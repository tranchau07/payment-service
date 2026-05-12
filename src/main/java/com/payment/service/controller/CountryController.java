package com.payment.service.controller;

import com.payment.service.dto.response.CountryResponse;
import com.payment.service.service.CountryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CountryController extends BaseCatalogController<CountryResponse, CountryService> {
    CountryService countryService;

    @Override
    protected CountryService getService() {
        return countryService;
    }
}
