package com.payment.service.service;

import com.payment.service.dto.response.CountryResponse;
import com.payment.service.entity.Country;
import com.payment.service.mapper.CountryMapper;
import com.payment.service.repository.BaseRepository;
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
public class CountryService extends BaseCatalogService<Country, CountryResponse> {
    CountryRepository countryRepository;
    CountryMapper countryMapper;

    @Override
    protected BaseRepository<Country, Long> getRepository() {
        return countryRepository;
    }

    @Override
    protected CountryResponse toResponse(Country entity) {
        return countryMapper.toCountryResponse(entity);
    }

    @Override
    protected String getEntityName() {
        return "Country";
    }

    public CountryResponse getCountryByCode(String code) {
        return getByCode(code);
    }

    public List<CountryResponse> getAllSa() {
        return getAll();
    }
}
