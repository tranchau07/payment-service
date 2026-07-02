package com.payment.service.controller;

import com.payment.service.dto.response.ProductConfigurationResponse;
import com.payment.service.service.ProductConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product-configurations")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('TELLER', 'SUPERVISOR', 'ADMIN')")
public class ProductConfigurationController {
    private final ProductConfigurationService service;

    @GetMapping("/issuing")
    public List<ProductConfigurationResponse> getIssuing(
            @RequestParam(defaultValue = "false") boolean includeNotReady) {
        return service.getProducts(ProductConfigurationService.BusinessType.ISSUING, includeNotReady);
    }

    @GetMapping("/acquiring")
    public List<ProductConfigurationResponse> getAcquiring(
            @RequestParam(defaultValue = "false") boolean includeNotReady) {
        return service.getProducts(ProductConfigurationService.BusinessType.ACQUIRING, includeNotReady);
    }

    @GetMapping("/{code}")
    public ProductConfigurationResponse getByCode(@PathVariable String code) {
        return service.getByCode(code);
    }
}
