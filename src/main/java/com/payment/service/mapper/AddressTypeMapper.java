package com.payment.service.mapper;

import com.payment.service.dto.response.AddressTypeResponse;
import com.payment.service.entity.AddressType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressTypeMapper {
    AddressTypeResponse toAddressTypeResponse(AddressType add);
    List<AddressTypeResponse> toAddressTypeResponses(List<AddressType> addList);
}
