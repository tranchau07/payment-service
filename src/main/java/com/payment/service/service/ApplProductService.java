package com.payment.service.service;

import com.payment.service.dto.response.ApplProductResponse;
import com.payment.service.mapper.ApplProductMapper;
import com.payment.service.repository.ApplProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplProductService {

    ApplProductRepository applProductRepository;
    ApplProductMapper applProductMapper;

    public List<ApplProductResponse> getAllProducts() {
        log.info("Fetching all products from APPL_PRODUCT with IS_READY='Y' and AMND_STATE='A'");
        return applProductMapper.toResponses(applProductRepository.findAllByIsReadyAndAmndState("Y", "A"));
    }

    public ApplProductResponse getProductByCode(String code) {
        log.info("Fetching product with code: {}", code);
        return applProductRepository.findByCode(code)
                .map(applProductMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Product not found with code: " + code));
    }
}
