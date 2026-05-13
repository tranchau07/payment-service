package com.payment.service.controller;

import com.payment.service.dto.request.CreateIssuingContractWithLiabilityRequest;
import com.payment.service.dto.response.ContractResponse;
import com.payment.service.dto.response.CreateIssuingContractWithLiabilityResponse;
import com.payment.service.service.ContractService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContractController {

    ContractService contractService;

    @GetMapping("/{contractNumber}")
    public ContractResponse getContractByNumber(@PathVariable String contractNumber) {
        return contractService.getContractByNumber(contractNumber);
    }

    @PostMapping("/create-with-liability")
    public CreateIssuingContractWithLiabilityResponse createIssuingContractWithLiability(@RequestBody CreateIssuingContractWithLiabilityRequest request) {
        return contractService.createIssuingContractWithLiability(request);
    }
}
