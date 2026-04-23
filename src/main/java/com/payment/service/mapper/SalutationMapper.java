package com.payment.service.mapper;

import com.payment.service.dto.response.LangResponse;
import com.payment.service.dto.response.SalutationResponse;
import com.payment.service.entity.Lang;
import com.payment.service.entity.Salutation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SalutationMapper {
    SalutationResponse toSalutationResponse(Salutation sa);
    List<SalutationResponse> toSalutationResponses(List<Salutation> saList);
}
