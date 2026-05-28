package com.payment.service.controller;

import com.payment.service.dto.request.CreateCardRequest;
import com.payment.service.dto.response.CreateCardResponse;
import com.payment.service.service.CardIntegrationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@PreAuthorize("hasAnyRole('TELLER', 'SUPERVISOR', 'ADMIN')")
public class CardController {

    CardIntegrationService cardIntegrationService;

    @PostMapping
    public CreateCardResponse createCard(@jakarta.validation.Valid @RequestBody CreateCardRequest request) {
        log.info("Nhận yêu cầu tạo thẻ mới cho hợp đồng: {}", request.getContractIdentifier());
        return cardIntegrationService.createCard(request);
    }
}
