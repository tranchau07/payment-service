package com.payment.service.service;

import com.payment.service.dto.request.CreateIssuingContractWithLiabilityRequest;
import com.payment.service.dto.response.ContractResponse;
import com.payment.service.dto.response.CreateIssuingContractWithLiabilityResponse;
import com.payment.service.mapper.ContractMapper;
import com.payment.service.repository.AcntContractRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContractService {

    AcntContractRepository contractRepository;
    ContractMapper contractMapper;
    ClientIntegrationService clientIntegrationService;

    public ContractResponse getContractByNumber(String contractNumber) {
        log.info("Fetching contract with number: {}", contractNumber);
        return contractRepository.findByContractNumber(contractNumber)
                .map(contractMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Contract not found with number: " + contractNumber));
    }

    public CreateIssuingContractWithLiabilityResponse createIssuingContractWithLiability(CreateIssuingContractWithLiabilityRequest request) {
        return clientIntegrationService.createIssuingContractWithLiability(request);
    }
}
