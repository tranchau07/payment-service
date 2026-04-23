package com.payment.service.mapper;

import com.payment.service.dto.response.MaritalStatusResponse;
import com.payment.service.entity.MaritalStatus;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MaritalStatusMapper {
    MaritalStatusResponse toMaritalStatusResponse(MaritalStatus ma);
    List<MaritalStatusResponse> toMaritalStatusResponses(List<MaritalStatus> maList);
}
