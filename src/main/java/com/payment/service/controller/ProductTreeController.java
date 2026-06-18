package com.payment.service.controller;

import com.payment.service.dto.response.ProductTreeNodeDto;
import com.payment.service.service.ProductTreeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@PreAuthorize("hasAnyRole('TELLER', 'SUPERVISOR', 'ADMIN')")
public class ProductTreeController {

    ProductTreeService productTreeService;

    @GetMapping("/tree")
    public ResponseEntity<List<ProductTreeNodeDto>> getProductTree() {
        log.info("Request to fetch Way4 product hierarchy tree.");
        return ResponseEntity.ok(productTreeService.getProductTree());
    }
}
