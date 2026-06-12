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
}
