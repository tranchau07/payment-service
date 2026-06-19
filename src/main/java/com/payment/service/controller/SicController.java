package com.payment.service.controller;

import com.payment.service.dto.request.SicSearchRequest;
import com.payment.service.dto.response.SicResponse;
import com.payment.service.service.SicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sics")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SicController {

    SicService sicService;

    @GetMapping
    public List<SicResponse> getAllSics(SicSearchRequest request) {
        return sicService.searchSics(request);
    }
}
