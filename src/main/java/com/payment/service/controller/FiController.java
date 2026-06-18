package com.payment.service.controller;

import com.payment.service.dto.response.FiResponse;
import com.payment.service.service.FiService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fis")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FiController {
    FiService fiService;

    @GetMapping("")
    public List<FiResponse> getAll() {
        return fiService.getAll();
    }
}
