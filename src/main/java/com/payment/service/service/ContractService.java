package com.payment.service.service;

import com.payment.service.entity.Client;
import com.payment.service.entity.AcntContract;
import com.payment.service.entity.Sic;
import com.payment.service.entity.ApplProduct;
import com.payment.service.entity.Currency;
import com.payment.service.repository.ClientRepository;
import com.payment.service.repository.AcntContractRepository;
import com.payment.service.repository.ApplProductRepository;
import com.payment.service.repository.SicRepository;
import com.payment.service.repository.CurrencyRepository;
import com.payment.service.mapper.ContractMapper;
import com.payment.service.dto.request.CreateIssuingContractWithLiabilityRequest;
import com.payment.service.dto.request.CreateLiabilityContractRequest;
import com.payment.service.dto.request.CreateAcquiringContractRequest;
import com.payment.service.dto.request.CreateAcquiringContractAddressRequest;
import com.payment.service.dto.request.CreateDeviceRequest;
import com.payment.service.dto.response.ContractResponse;
import com.payment.service.dto.response.CreateIssuingContractWithLiabilityResponse;
import com.payment.service.dto.response.LiabilityCheckResponse;
import com.payment.service.dto.response.CreateContractResponse;
import com.payment.service.dto.response.CreateAcquiringContractResponse;
import com.payment.service.dto.response.CreateAcquiringContractAddressResponse;
import com.payment.service.dto.response.CreateDeviceResponse;
import com.payment.service.exception.AppException;
import com.payment.service.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

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
    CurrencyRepository currencyRepository;

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
        sicRepository.findByCodeAndAmndState(mcc, "A")
                .filter(s -> "Y".equals(s.getUseInBank()))
                .orElseThrow(() -> {
                    log.error("Mã ngành nghề MCC (SIC) không hợp lệ hoặc không dùng trong ngân hàng: {}", mcc);
                    return new AppException(ErrorCode.MCC_NOT_FOUND, null, "Mã ngành nghề MCC không tồn tại hoặc không được phép sử dụng trong ngân hàng: " + mcc, null);
                });

        // 7. Xác thực Mã tiền tệ (Currency)
        String currencyReq = request.getInObject().getCurrency();
        if (currencyReq == null || currencyReq.trim().isEmpty()) {
            log.error("Mã tiền tệ (Currency) là bắt buộc");
            throw new AppException(ErrorCode.INVALID_REQUEST_DATA, null, "Mã tiền tệ (Currency) là bắt buộc", null);
        }
        Currency currency = currencyRepository.findByNumericCode(currencyReq.trim())
                .or(() -> currencyRepository.findByCode(currencyReq.trim()))
                .filter(c -> "Y".equals(c.getIsActive()))
                .orElseThrow(() -> {
                    log.error("Mã tiền tệ không hợp lệ hoặc không hoạt động: {}", currencyReq);
                    return new AppException(ErrorCode.CURRENCY_NOT_FOUND, null, "Mã tiền tệ không tồn tại hoặc không được phép sử dụng: " + currencyReq, null);
                });

        // Ánh xạ lại thành mã tiền tệ số (ISO Numeric Code) mà Core WAY4 yêu cầu
        request.getInObject().setCurrency(currency.getNumericCode());

        // Chuẩn hóa và loại bỏ dấu tiếng Việt để tương thích với Core SOAP (tránh lỗi Invalid characters)
        request.getInObject().setContractName(cleanAddressField(request.getInObject().getContractName()));
        request.getInObject().setMerchantId(cleanAddressField(request.getInObject().getMerchantId()));

        log.info("Tất cả xác thực trước khi tạo hợp đồng đều hợp lệ. Tiến hành gửi SOAP.");
        return clientIntegrationService.createAcquiringContractV2(request);
    }

    public CreateAcquiringContractAddressResponse createAcquiringContractAddress(
            String contractNumber,
            CreateAcquiringContractAddressRequest request) {
        String normalizedContractNumber = contractNumber == null ? "" : contractNumber.trim();
        if (normalizedContractNumber.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST_DATA, null, "Số hợp đồng là bắt buộc", null);
        }

        contractRepository.findByContractNumberAndAmndState(normalizedContractNumber, "A")
                .orElseThrow(() -> new AppException(
                        ErrorCode.INVALID_REQUEST_DATA,
                        null,
                        "Hợp đồng không tồn tại hoặc không ở trạng thái hoạt động: " + normalizedContractNumber,
                        null
                ));

        // Chuẩn hóa và loại bỏ dấu tiếng Việt để tương thích với Core SOAP (tránh lỗi Invalid characters)
        request.setCity(cleanAddressField(request.getCity()));
        request.setDistrict(cleanAddressField(request.getDistrict()));
        request.setLine1(cleanAddressField(request.getLine1()));
        request.setLine2(cleanAddressField(request.getLine2()));
        request.setLine3(cleanAddressField(request.getLine3()));
        request.setLine4(cleanAddressField(request.getLine4()));
        request.setReason(cleanAddressField(request.getReason()));

        String correlationId = java.util.UUID.randomUUID().toString();
        return clientIntegrationService.createAcquiringContractAddress(normalizedContractNumber, request, correlationId);
    }

    public CreateDeviceResponse createDevice(CreateDeviceRequest request) {
        String parentContractNum = request.getContractIdentifier();
        if (parentContractNum == null || parentContractNum.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST_DATA, null, "Số hợp đồng cha (contractIdentifier) là bắt buộc", null);
        }

        // 1. Tìm hợp đồng cha
        AcntContract parentContract = contractRepository.findByContractNumberAndAmndState(parentContractNum.trim(), "A")
                .orElseThrow(() -> new AppException(
                        ErrorCode.INVALID_REQUEST_DATA,
                        null,
                        "Hợp đồng cha không tồn tại hoặc không ở trạng thái hoạt động: " + parentContractNum,
                        null
                ));

        // 2. Lấy thông tin sản phẩm của hợp đồng cha
        String productStr = parentContract.getProduct();
        if (productStr == null || productStr.length() <= 6) {
            throw new AppException(
                    ErrorCode.INVALID_REQUEST_DATA,
                    null,
                    "Hợp đồng cha không có cấu hình sản phẩm hợp lệ",
                    null
            );
        }
        long parentProductOid;
        try {
            parentProductOid = Long.parseLong(productStr.substring(6));
        } catch (NumberFormatException e) {
            throw new AppException(
                    ErrorCode.INVALID_REQUEST_DATA,
                    null,
                    "Mã sản phẩm của hợp đồng cha không hợp lệ: " + productStr,
                    null
            );
        }

        ApplProduct parentProduct = applProductRepository.findById(parentProductOid)
                .orElseThrow(() -> new AppException(
                        ErrorCode.INVALID_REQUEST_DATA,
                        null,
                        "Không tìm thấy thông tin sản phẩm của hợp đồng cha trong hệ thống",
                        null
                ));
        if (!"M".equalsIgnoreCase(parentProduct.getConCat()) && !"M".equalsIgnoreCase(parentProduct.getPcat())) {
            throw new AppException(ErrorCode.INVALID_REQUEST_DATA, null,
                    "Hop dong cha khong phai hop dong Acquiring", null);
        }

        // 3. Tìm sản phẩm Device
        String deviceProductCode = request.getProductCode();
        ApplProduct deviceProduct = applProductRepository.findByCode(deviceProductCode)
                .filter(p -> "A".equals(p.getAmndState()) && !"N".equals(p.getIsActive()))
                .orElseThrow(() -> new AppException(
                        ErrorCode.INVALID_REQUEST_DATA,
                        null,
                        "Sản phẩm Device không tồn tại: " + deviceProductCode,
                        null
                ));
        if (!"T".equalsIgnoreCase(deviceProduct.getConCat()) && ! "T".equalsIgnoreCase(deviceProduct.getPcat())
                && !("M".equalsIgnoreCase(deviceProduct.getConCat()) && "M".equalsIgnoreCase(deviceProduct.getPcat()))) {
            throw new AppException(ErrorCode.INVALID_REQUEST_DATA, null,
                    "San pham khong thuoc nhom Device/Terminal: " + deviceProductCode, null);
        }

        // 4. Kiểm tra sản phẩm Device có phải là con của sản phẩm hợp đồng cha hay không
        String parentProductCode = parentProduct.getCode();
        boolean isChildProduct = false;
        if (deviceProduct.getParentCode() != null) {
            if (deviceProduct.getParentCode().equals(parentProductCode)) {
                isChildProduct = true;
            } else if (deviceProduct.getParentCode().length() == 24 && deviceProduct.getParentCode().startsWith("161103")) {
                try {
                    long parentOid = Long.parseLong(deviceProduct.getParentCode().substring(6));
                    if (parentOid == parentProduct.getId()) {
                        isChildProduct = true;
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        if (!isChildProduct) {
            log.error("Sản phẩm Device {} không phải là con của sản phẩm hợp đồng cha {}", deviceProductCode, parentProductCode);
            throw new AppException(
                    ErrorCode.INVALID_REQUEST_DATA,
                    null,
                    "Sản phẩm Device (" + deviceProductCode + ") phải là sản phẩm con của hợp đồng cha (" + parentProductCode + ")",
                    null
            );
        }

        // 5. Xác thực và ánh xạ Mã tiền tệ (Currency)
        String currencyReq = request.getInObject().getDefaultCurrency();
        if (currencyReq == null || currencyReq.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST_DATA, null, "Mã tiền tệ (DefaultCurrency) là bắt buộc", null);
        }
        Currency currency = currencyRepository.findByNumericCode(currencyReq.trim())
                .or(() -> currencyRepository.findByCode(currencyReq.trim()))
                .filter(c -> "Y".equals(c.getIsActive()))
                .orElseThrow(() -> {
                    log.error("Mã tiền tệ không hợp lệ hoặc không hoạt động: {}", currencyReq);
                    return new AppException(ErrorCode.CURRENCY_NOT_FOUND, null, "Mã tiền tệ không tồn tại hoặc không được phép sử dụng: " + currencyReq, null);
                });

        // Ánh xạ lại thành mã tiền tệ số (ISO Numeric Code)
        request.getInObject().setDefaultCurrency(currency.getNumericCode());

        int start = Integer.parseInt(request.getInObject().getStartTime());
        int end = Integer.parseInt(request.getInObject().getEndTime());
        int cutOff = Integer.parseInt(request.getInObject().getCutOffTime());
        if (start >= end || cutOff < start || cutOff > end) {
            throw new AppException(ErrorCode.INVALID_REQUEST_DATA, null,
                    "Khung gio Device khong hop le: start < cutOff <= end", null);
        }

        log.info("Xác thực sản phẩm và tiền tệ của Device thành công. Gửi yêu cầu tạo qua SOAP.");
        return clientIntegrationService.createDevice(request);
    }

    private static String removeAccents(String src) {
        if (src == null) {
            return null;
        }
        String nfdNormalizedString = Normalizer.normalize(src, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(nfdNormalizedString).replaceAll("");
        result = result.replace("Đ", "D").replace("đ", "d");
        return result;
    }

    private String cleanAddressField(String value) {
        if (value == null) {
            return null;
        }
        String cleaned = removeAccents(value.trim());
        cleaned = cleaned.replaceAll("[^\\x00-\\x7F]", "");
        return cleaned;
    }
}
