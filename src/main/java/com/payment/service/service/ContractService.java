package com.payment.service.service;

import com.payment.service.entity.Client;
import com.payment.service.entity.AcntContract;
import com.payment.service.repository.ClientRepository;
import com.payment.service.repository.AcntContractRepository;
import com.payment.service.repository.ApplProductRepository;
import com.payment.service.mapper.ContractMapper;
import com.payment.service.dto.request.CreateIssuingContractWithLiabilityRequest;
import com.payment.service.dto.request.CreateLiabilityContractRequest;
import com.payment.service.dto.response.ContractResponse;
import com.payment.service.dto.response.CreateIssuingContractWithLiabilityResponse;
import com.payment.service.dto.response.LiabilityCheckResponse;
import com.payment.service.dto.response.CreateContractResponse;
import com.payment.service.exception.AppException;
import com.payment.service.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.payment.service.entity.Sic;
import com.payment.service.entity.ApplProduct;
import com.payment.service.repository.SicRepository;
import com.payment.service.dto.request.CreateAcquiringContractRequest;
import com.payment.service.dto.response.CreateAcquiringContractResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContractService {

    AcntContractRepository contractRepository;
    ClientRepository clientRepository;
    ContractMapper contractMapper;
    ClientIntegrationService clientIntegrationService;
    ApplProductRepository applProductRepository;
    SicRepository sicRepository;

    private Optional<Client> findActiveClientByNumber(String clientNumber) {
        List<Client> clients = clientRepository.findByClientNumberAndAmndState(clientNumber, "A");
        if (clients.isEmpty()) {
            return Optional.empty();
        }
        if (clients.size() > 1) {
            log.warn("Found {} active clients for clientNumber {}. Selecting the one with highest ID.", clients.size(), clientNumber);
            return clients.stream().max(java.util.Comparator.comparing(Client::getId));
        }
        return Optional.of(clients.get(0));
    }

    public ContractResponse getContractByNumber(String contractNumber) {
        log.info("Fetching contract with number: {}", contractNumber);
        return contractRepository.findByContractNumberAndAmndState(contractNumber, "A")
                .map(contractMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Contract not found with number: " + contractNumber));
    }

    public CreateIssuingContractWithLiabilityResponse createIssuingContractWithLiability(CreateIssuingContractWithLiabilityRequest request) {
        String clientIdentifier = request.getClientIdentifier();
        log.info("Creating issuing contract. Client identifier from request: {}", clientIdentifier);
        if (clientIdentifier != null) {
            Optional<Client> clientOpt = findActiveClientByNumber(clientIdentifier);
            if (clientOpt.isPresent()) {
                String internalId = String.valueOf(clientOpt.get().getId());
                log.info("Resolved clientNumber {} to internal Client ID {}", clientIdentifier, internalId);
                request.setClientIdentifier(internalId);
            }
        }
        return clientIntegrationService.createIssuingContractWithLiability(request);
    }

    @Transactional(readOnly = true)
    public LiabilityCheckResponse checkClientLiability(String clientNumber) {
        log.info("Checking liability contract for client: {}", clientNumber);
        Client client = findActiveClientByNumber(clientNumber)
                .orElseThrow(() -> new AppException(ErrorCode.CLIENT_NOT_FOUND, null, "Không tìm thấy khách hàng: " + clientNumber, null));

        List<AcntContract> contracts = contractRepository.findByClientIdAndContractLevelAndAmndState(client.getId(), ".1.", "A");
        
        Optional<AcntContract> liabilityContractOpt = contracts.stream()
                .filter(contract -> {
                    String productStr = contract.getProduct();
                    if (productStr != null && productStr.length() > 6) {
                        try {
                            long parsedId = Long.parseLong(productStr.substring(6));
                            return applProductRepository.findById(parsedId)
                                    .map(p -> "LIAB_TRAINING01".equals(p.getCode()))
                                    .orElse(false);
                        } catch (NumberFormatException e) {
                            log.warn("Invalid product string format in contract: {}", productStr);
                        }
                    }
                    return false;
                })
                .findFirst();

        if (liabilityContractOpt.isEmpty()) {
            return LiabilityCheckResponse.builder()
                    .hasLiability(false)
                    .build();
        }

        AcntContract liabilityContract = liabilityContractOpt.get();
        return LiabilityCheckResponse.builder()
                .hasLiability(true)
                .contractNumber(liabilityContract.getContractNumber())
                .contractName(liabilityContract.getContractName())
                .cbsNumber(liabilityContract.getRbsNumber())
                .build();
    }

    public CreateContractResponse createLiabilityContract(CreateLiabilityContractRequest request) {
        String correlationId = UUID.randomUUID().toString();
        log.info("Bắt đầu tạo hợp đồng bảo lãnh cho Client: {}. CorrelationID: {}", request.getClientNumber(), correlationId);

        Client client = findActiveClientByNumber(request.getClientNumber())
                .orElseThrow(() -> new AppException(ErrorCode.CLIENT_NOT_FOUND, null, "Không tìm thấy khách hàng: " + request.getClientNumber(), null));

        String branch = client.getBranchCode() != null ? client.getBranchCode() : "0101";
        String institutionCode = "0001";
        if (client.getParentId() != null) {
            institutionCode = String.format("%04d", client.getParentId());
        }

        CreateContractResponse response = clientIntegrationService.createLiabilityContractViaSoap(
                client.getId(),
                branch,
                institutionCode,
                request.getCbsNumber(),
                correlationId
        );

        if (!response.isSuccess()) {
            log.error("Tạo hợp đồng bảo lãnh thất bại: {}. CorrelationID: {}", response.getRetMsg(), correlationId);
            throw new AppException(
                ErrorCode.CORE_CONTRACT_CREATION_FAILED,
                correlationId,
                "Tạo hợp đồng bảo lãnh thất bại: " + response.getRetMsg(),
                response.getRetMsg()
            );
        }

        return response;
    }

    public CreateAcquiringContractResponse createAcquiringContractV2(CreateAcquiringContractRequest request) {
        log.info("Bắt đầu thực hiện tạo Acquiring Contract. ClientIdentifier: {}", request.getClientIdentifier());

        // 1. Xác thực Khách hàng (Client)
        Long clientId;
        try {
            clientId = Long.parseLong(request.getClientIdentifier());
        } catch (NumberFormatException e) {
            log.error("ClientIdentifier không hợp lệ: {}", request.getClientIdentifier());
            throw new AppException(ErrorCode.CLIENT_NOT_FOUND, null, "Mã khách hàng (Client Identifier) không đúng định dạng số: " + request.getClientIdentifier(), null);
        }

        Client client = clientRepository.findById(clientId)
                .filter(c -> "A".equals(c.getAmndState()))
                .orElseThrow(() -> {
                    log.error("Không tìm thấy khách hàng hoạt động với ID: {}", clientId);
                    return new AppException(ErrorCode.CLIENT_NOT_FOUND, null, "Không tìm thấy khách hàng hoạt động với ID: " + clientId, null);
                });

        // 2. Xác thực Số hợp đồng tính duy nhất (nếu truyền vào)
        String contractNumber = request.getInObject().getContractNumber();
        if (contractNumber != null && !contractNumber.trim().isEmpty()) {
            Optional<AcntContract> existingContract = contractRepository.findByContractNumberAndAmndState(contractNumber.trim(), "A");
            if (existingContract.isPresent()) {
                log.error("Số hợp đồng đã tồn tại và đang hoạt động: {}", contractNumber);
                throw new AppException(ErrorCode.CONTRACT_ALREADY_EXISTS, null, "Số hợp đồng đã tồn tại trong hệ thống: " + contractNumber, null);
            }
        }

        // 3. Xác thực MerchantID tính duy nhất
        String merchantId = request.getInObject().getMerchantId();
        if (merchantId != null && !merchantId.trim().isEmpty()) {
            Optional<AcntContract> existingMerchant = contractRepository.findByMerchantIdAndAmndState(merchantId.trim(), "A");
            if (existingMerchant.isPresent()) {
                log.error("MerchantID đã tồn tại và đang hoạt động: {}", merchantId);
                throw new AppException(ErrorCode.MERCHANT_ID_ALREADY_EXISTS, null, "Mã MerchantID đã tồn tại trong hệ thống: " + merchantId, null);
            }
        }

        // 4. Xác thực CBSNumber tính duy nhất
        String cbsNumber = request.getInObject().getCbsNumber();
        if (cbsNumber != null && !cbsNumber.trim().isEmpty()) {
            Optional<AcntContract> existingCbs = contractRepository.findByRbsNumberAndAmndState(cbsNumber.trim(), "A");
            if (existingCbs.isPresent()) {
                log.error("CBSNumber đã tồn tại và đang hoạt động: {}", cbsNumber);
                throw new AppException(ErrorCode.CBS_NUMBER_ALREADY_EXISTS, null, "Số CBSNumber đã tồn tại trong hệ thống: " + cbsNumber, null);
            }
        }

        // 5. Xác thực Sản phẩm hợp đồng (Product Code)
        ApplProduct product = applProductRepository.findByCode(request.getProductCode())
                .filter(p -> "A".equals(p.getAmndState()) && !"N".equals(p.getIsActive()))
                .orElseThrow(() -> {
                    log.error("Sản phẩm không tồn tại hoặc đã bị khóa: {}", request.getProductCode());
                    return new AppException(ErrorCode.PRODUCT_NOT_FOUND, null, "Sản phẩm không tồn tại hoặc đã bị khóa: " + request.getProductCode(), null);
                });

        // Đảm bảo sản phẩm thuộc nhóm chấp nhận thanh toán (Merchant/Terminal)
        if (!"M".equalsIgnoreCase(product.getConCat()) && !"M".equalsIgnoreCase(product.getPcat()) && !"T".equalsIgnoreCase(product.getConCat())) {
            log.error("Sản phẩm không thuộc nhóm Acquiring: conCat={}, pcat={}", product.getConCat(), product.getPcat());
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND, null, "Sản phẩm không thuộc nhóm sản phẩm Acquiring: " + request.getProductCode(), null);
        }

        // 6. Xác thực Mã ngành nghề (MCC / SIC)
        String mcc = request.getInObject().getMcc();
        Sic sic = sicRepository.findByCodeAndAmndState(mcc, "A")
                .filter(s -> "Y".equals(s.getUseInBank()))
                .orElseThrow(() -> {
                    log.error("Mã ngành nghề MCC (SIC) không hợp lệ hoặc không dùng trong ngân hàng: {}", mcc);
                    return new AppException(ErrorCode.MCC_NOT_FOUND, null, "Mã ngành nghề MCC không tồn tại hoặc không được phép sử dụng trong ngân hàng: " + mcc, null);
                });

        // 7. Xác thực Địa chỉ (Bắt buộc và verify region có độ dài 2 ký tự)
        CreateAcquiringContractRequest.AddressObject address = request.getAddress();
        if (address == null) {
            log.error("Thông tin địa chỉ (address) là bắt buộc");
            throw new AppException(ErrorCode.INVALID_REQUEST_DATA, null, "Thông tin địa chỉ (address) là bắt buộc", null);
        }
        String region = address.getRegion();
        if (region == null || region.trim().length() != 2) {
            log.error("Mã vùng (Region) phải có độ dài đúng 2 ký tự: {}", region);
            throw new AppException(ErrorCode.INVALID_REQUEST_DATA, null, "Mã vùng (Region) phải có độ dài đúng 2 ký tự", null);
        }

        log.info("Tất cả xác thực trước khi tạo hợp đồng đều hợp lệ. Tiến hành gửi SOAP.");

        // 1. Tạo hợp đồng
        CreateAcquiringContractResponse contractResponse = clientIntegrationService.createAcquiringContractV2(request);

        // 2. Tự động tạo địa chỉ hợp đồng
        String correlationId = java.util.UUID.randomUUID().toString();
        try {
            log.info("Tự động tạo địa chỉ cho hợp đồng: {}. CorrelationID: {}", contractResponse.getContractNumber(), correlationId);
            clientIntegrationService.createAcquiringContractAddress(contractResponse.getContractNumber(), address, correlationId);
            contractResponse.setAddressCreationStatus("SUCCESS");
        } catch (Exception e) {
            log.error("Tự động tạo địa chỉ hợp đồng thất bại: {}. CorrelationID: {}", e.getMessage(), correlationId);
            contractResponse.setAddressCreationStatus("FAILED: " + e.getMessage());
        }

        return contractResponse;
    }
}
