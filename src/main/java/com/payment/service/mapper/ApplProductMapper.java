package com.payment.service.mapper;

import com.payment.service.dto.response.ApplProductResponse;
import com.payment.service.entity.ApplProduct;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplProductMapper {
    ApplProductResponse toResponse(ApplProduct entity);
    List<ApplProductResponse> toResponses(List<ApplProduct> entities);
}
