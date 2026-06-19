package com.payment.service.mapper;

import com.payment.service.dto.response.SicResponse;
import com.payment.service.entity.Sic;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SicMapper {
    SicResponse toResponse(Sic entity);
    List<SicResponse> toResponses(List<Sic> entities);
}
