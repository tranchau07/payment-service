package com.payment.service.service;

import com.payment.service.dto.response.LangResponse;
import com.payment.service.mapper.LangMapper;
import com.payment.service.repository.LangRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class LangService {
    LangRepository langRepository;
    LangMapper langMapper;

    public LangResponse getLangByCode(String code) {
        log.info("Getting language by code: {}", code);
        return langMapper.toLangResponse(langRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Language not found with code: " + code)));
    }

    public List<LangResponse> getAllLang() {
        return langMapper.toLangResponses(langRepository.findAll());
    }
}
