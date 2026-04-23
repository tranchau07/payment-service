package com.payment.service.mapper;

import com.payment.service.dto.response.LangResponse;
import com.payment.service.entity.Lang;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface LangMapper {
    LangResponse toLangResponse(Lang lang);
    List<LangResponse> toLangResponses(List<Lang> langList);
}
