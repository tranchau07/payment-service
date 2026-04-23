package com.payment.service.controller;

import com.payment.service.dto.response.SalutationResponse;
import com.payment.service.service.LangService;
import com.payment.service.service.SalutationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/salutations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SalutationController {
    SalutationService salutationService;

    @GetMapping("/{code}")
    SalutationResponse getLangByCode(@PathVariable("code") String code) {
        return salutationService.getSalutationByCode(code);
    }

    @GetMapping("")
    List<SalutationResponse> getAllLang() {
        return salutationService.getAllSa();
    }
}
