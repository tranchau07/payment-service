package com.payment.service.mapper;

import com.payment.service.dto.response.ContractResponse;
import com.payment.service.entity.AcntContract;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    ContractResponse toResponse(AcntContract entity);
}
