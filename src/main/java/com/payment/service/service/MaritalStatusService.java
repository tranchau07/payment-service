package com.payment.service.service;

import com.payment.service.dto.response.MaritalStatusResponse;
import com.payment.service.mapper.MaritalStatusMapper;
import com.payment.service.repository.MaritalStatusRepository;
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
public class MaritalStatusService {
    MaritalStatusRepository maritalStatusRepository;
    MaritalStatusMapper maritalStatusMapper;

    public MaritalStatusResponse getMaritalStatusByCode(String code) {
        log.info("Getting by code: {}", code);
        return maritalStatusMapper.toMaritalStatusResponse(maritalStatusRepository.findByCode(code).orElseThrow(RuntimeException::new
                ));
    }

    public List<MaritalStatusResponse> getAllSa() {
        return maritalStatusMapper.toMaritalStatusResponses(maritalStatusRepository.findAll());
    }
}
