package com.payment.service.mapper;

import com.payment.service.dto.response.CurrencyResponse;
import com.payment.service.entity.Currency;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyResponse toCurrencyResponse(Currency currency);
    List<CurrencyResponse> toCurrencyResponses(List<Currency> currencies);
}
