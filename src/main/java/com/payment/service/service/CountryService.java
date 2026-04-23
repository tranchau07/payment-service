package com.payment.service.service;

import com.payment.service.dto.response.CountryResponse;
import com.payment.service.mapper.CountryMapper;
import com.payment.service.repository.CountryRepository;
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
public class CountryService {
    CountryRepository countryRepository;
    CountryMapper countryMapper;

    public CountryResponse getCountryByCode(String code) {
        log.info("Getting by code: {}", code);
        return countryMapper.toCountryResponse(countryRepository.findByCode(code).orElseThrow(RuntimeException::new
                ));
    }

    public List<CountryResponse> getAllSa() {
        return countryMapper.toCountryResponses(countryRepository.findAll());
    }
}
