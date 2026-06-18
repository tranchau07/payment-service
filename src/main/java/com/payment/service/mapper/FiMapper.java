package com.payment.service.mapper;

import com.payment.service.dto.response.FiResponse;
import com.payment.service.entity.Fi;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FiMapper {
    FiResponse toFiResponse(Fi fi);
    List<FiResponse> toFiResponses(List<Fi> fiList);
}
