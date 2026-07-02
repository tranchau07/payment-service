package com.payment.service.controller;

import com.payment.service.dto.request.CreateIssuingContractWithLiabilityRequest;
import com.payment.service.dto.response.ContractResponse;
import com.payment.service.dto.response.CreateIssuingContractWithLiabilityResponse;
import com.payment.service.service.ContractService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.payment.service.dto.request.CreateLiabilityContractRequest;
import com.payment.service.dto.response.LiabilityCheckResponse;
import com.payment.service.dto.response.CreateContractResponse;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasAnyRole('TELLER', 'SUPERVISOR', 'ADMIN')")
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

    @GetMapping("/liability-by-client/{clientNumber}")
    public LiabilityCheckResponse checkClientLiability(@PathVariable String clientNumber) {
        return contractService.checkClientLiability(clientNumber);
    }

    @PostMapping("/create-liability")
    public CreateContractResponse createLiabilityContract(@RequestBody CreateLiabilityContractRequest request) {
        return contractService.createLiabilityContract(request);
    }

    @PostMapping("/acquiring")
    public com.payment.service.dto.response.CreateAcquiringContractResponse createAcquiringContractV2(
            @jakarta.validation.Valid @RequestBody com.payment.service.dto.request.CreateAcquiringContractRequest request) {
        return contractService.createAcquiringContractV2(request);
    }

    @PostMapping("/acquiring/{contractNumber}/addresses")
    public com.payment.service.dto.response.CreateAcquiringContractAddressResponse createAcquiringContractAddress(
            @PathVariable String contractNumber,
            @jakarta.validation.Valid @RequestBody com.payment.service.dto.request.CreateAcquiringContractAddressRequest request) {
        return contractService.createAcquiringContractAddress(contractNumber, request);
    }

    @PostMapping("/acquiring/{contractNumber}/devices")
    public com.payment.service.dto.response.CreateDeviceResponse createDevice(
            @PathVariable String contractNumber,
            @jakarta.validation.Valid @RequestBody com.payment.service.dto.request.CreateDeviceRequest request) {
        request.setContractSearchMethod("CONTRACT_NUMBER");
        request.setContractIdentifier(contractNumber);
        return contractService.createDevice(request);
    }
}
