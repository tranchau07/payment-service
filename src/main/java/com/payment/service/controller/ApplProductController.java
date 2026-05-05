package com.payment.service.controller;

import com.payment.service.dto.response.ApplProductResponse;
import com.payment.service.service.ApplProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appl-products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplProductController {

    ApplProductService applProductService;

    @GetMapping
    public List<ApplProductResponse> getAllProducts() {
        return applProductService.getAllProducts();
    }

    @GetMapping("/{code}")
    public ApplProductResponse getProductByCode(@PathVariable String code) {
        return applProductService.getProductByCode(code);
    }
}
