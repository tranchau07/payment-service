package com.payment.service.controller;

import com.payment.service.dto.response.LangResponse;
import com.payment.service.service.LangService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/langs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LangController {
    LangService langService;

    @GetMapping("/{code}")
    LangResponse getLangByCode(@PathVariable("code") String code) {
        return langService.getLangByCode(code);
    }

    @GetMapping("")
    List<LangResponse> getAllLang() {
        return langService.getAllLang();
    }
}
