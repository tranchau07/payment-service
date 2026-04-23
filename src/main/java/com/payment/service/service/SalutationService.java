package com.payment.service.service;

import com.payment.service.dto.response.SalutationResponse;
import com.payment.service.mapper.SalutationMapper;
import com.payment.service.repository.SalutationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class SalutationService {
    SalutationRepository salutationRepository;
    SalutationMapper salutationMapper;

    public SalutationResponse getSalutationByCode(String code) {
        log.info("Getting by code: {}", code);
        return salutationMapper.toSalutationResponse(salutationRepository.findByCode(code).orElseThrow(RuntimeException::new
                ));
    }

    public List<SalutationResponse> getAllSa() {
        return salutationMapper.toSalutationResponses(salutationRepository.findAll());
    }
}
