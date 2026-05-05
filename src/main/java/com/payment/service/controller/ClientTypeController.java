package com.payment.service.controller;

import com.payment.service.dto.response.ClientTypeResponse;
import com.payment.service.service.ClientTypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clientTypes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientTypeController {
    ClientTypeService clientTypeService;


    @GetMapping("")
    List<ClientTypeResponse> getAllClientType() {
        return clientTypeService.getAllClientType();
    }
}
