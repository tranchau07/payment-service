package com.payment.service.controller;

import com.payment.service.dto.request.CreateMerchantRequest;
import com.payment.service.dto.response.CreateMerchantResponse;
import com.payment.service.service.ClientIntegrationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchants")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@PreAuthorize("hasAnyRole('TELLER', 'SUPERVISOR', 'ADMIN')")
public class MerchantController {

    ClientIntegrationService clientIntegrationService;

    @PostMapping("/register")
    public CreateMerchantResponse registerMerchant(@jakarta.validation.Valid @RequestBody CreateMerchantRequest request) {
        log.info("Request to register merchant: {}", request);
        return clientIntegrationService.registerMerchantToCore(request);
    }
}
