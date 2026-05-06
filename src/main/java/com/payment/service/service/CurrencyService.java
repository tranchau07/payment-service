package com.payment.service.service;

import com.payment.service.dto.response.CurrencyResponse;
import com.payment.service.mapper.CurrencyMapper;
import com.payment.service.repository.CurrencyRepository;
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
public class CurrencyService {
    CurrencyRepository currencyRepository;
    CurrencyMapper currencyMapper;

    public List<CurrencyResponse> getAllCurrencies() {
        log.info("Fetching all currencies");
        return currencyMapper.toCurrencyResponses(currencyRepository.findAll());
    }

    public CurrencyResponse getCurrencyByCode(String code) {
        log.info("Fetching currency by code: {}", code);
        return currencyRepository.findByCode(code)
                .map(currencyMapper::toCurrencyResponse)
                .orElseThrow(() -> new RuntimeException("Currency not found with code: " + code));
    }
}
