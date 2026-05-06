package com.payment.service.controller;

import com.payment.service.dto.response.CurrencyResponse;
import com.payment.service.service.CurrencyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CurrencyController {
    CurrencyService currencyService;

    @GetMapping
    public List<CurrencyResponse> getAllCurrencies() {
        return currencyService.getAllCurrencies();
    }

    @GetMapping("/{code}")
    public CurrencyResponse getCurrencyByCode(@PathVariable String code) {
        return currencyService.getCurrencyByCode(code);
    }
}
