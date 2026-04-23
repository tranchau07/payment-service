package com.payment.service.mapper;

import com.payment.service.dto.response.CountryResponse;
import com.payment.service.entity.Country;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    CountryResponse toCountryResponse(Country con);
    List<CountryResponse> toCountryResponses(List<Country> conList);
}
