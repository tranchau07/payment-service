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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CardController {

    CardIntegrationService cardIntegrationService;

    @PostMapping
    public ResponseEntity<?> createCard(@RequestBody CreateCardRequest request) {
        log.info("Nhận yêu cầu tạo thẻ mới cho hợp đồng: {}", request.getContractIdentifier());
        try {
            CreateCardResponse response = cardIntegrationService.createCard(request);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                log.warn("Tạo thẻ thất bại từ Core: {}", response.getRetMsg());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
        } catch (Exception e) {
            log.error("Lỗi hệ thống khi xử lý tạo thẻ: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra trong quá trình tích hợp hệ thống: " + e.getMessage());
        }
    }
}
