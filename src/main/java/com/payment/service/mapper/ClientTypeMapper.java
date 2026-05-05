package com.payment.service.mapper;

import com.payment.service.dto.response.ClientTypeResponse;
import com.payment.service.entity.ClientType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientTypeMapper {
    ClientTypeResponse toClientTypeResponse(ClientType bra);
    List<ClientTypeResponse> toClientTypeResponses(List<ClientType> braList);
}
