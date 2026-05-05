package com.payment.service.mapper;

import com.payment.service.dto.response.ClientListResponse;
import com.payment.service.dto.response.ClientResponse;
import com.payment.service.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(source = "phoneMobile", target = "mobilePhone")
    @Mapping(source = "socialNumber", target = "maskedSocialNumber", qualifiedByName = "maskSensitiveData")
    @Mapping(source = "itn", target = "maskedItn", qualifiedByName = "maskSensitiveData")
    @Mapping(source = "dateOpen", target = "dateOpen", qualifiedByName = "mapLocalDateToLocalDateTime")
    ClientResponse toResponse(Client entity);

    List<ClientResponse> toResponseList(List<Client> entities);

    default ClientListResponse toListResponse(Page<Client> pageData) {
        if (pageData == null) {
            return null;
        }

        return ClientListResponse.builder()
                .data(toResponseList(pageData.getContent()))

                .currentPage(pageData.getNumber())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .hasNext(pageData.hasNext())

                .retCode(0L)
                .retMsg("Success")
                .build();
    }

    @Named("maskSensitiveData")
    default String maskSensitiveData(String data) {
        if (data == null || data.trim().isEmpty() || data.length() <= 4) {
            return data;
        }
        return "*".repeat(data.length() - 4) + data.substring(data.length() - 4);
    }

    @Named("mapLocalDateToLocalDateTime")
    default LocalDateTime mapLocalDateToLocalDateTime(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atStartOfDay();
    }
}