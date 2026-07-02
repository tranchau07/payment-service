package com.payment.service.controller;

import com.payment.service.dto.request.CreateMerchantRequest;
import com.payment.service.dto.request.MerchantSearchRequest;
import com.payment.service.dto.response.ClientListResponse;
import com.payment.service.dto.response.CreateMerchantResponse;
import com.payment.service.service.ClientIntegrationService;
import com.payment.service.service.MerchantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/merchants")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@PreAuthorize("hasAnyRole('TELLER', 'SUPERVISOR', 'ADMIN')")
public class MerchantController {

    ClientIntegrationService clientIntegrationService;
    MerchantService merchantService;

    @PostMapping("/register")
    public CreateMerchantResponse registerMerchant(
            @jakarta.validation.Valid @RequestBody CreateMerchantRequest request) {
        log.info("Request to register merchant: {}", request);
        return clientIntegrationService.registerMerchantToCore(request);
    }

    @GetMapping
    public ResponseEntity<ClientListResponse> searchMerchants(
            MerchantSearchRequest searchRequest,
            @PageableDefault(size = 20) Pageable pageable) {

        log.info("Received request to search merchants with criteria: {}", searchRequest);

        ClientListResponse response = merchantService.searchMerchants(searchRequest, pageable);

        return ResponseEntity.ok(response);
    }
}
