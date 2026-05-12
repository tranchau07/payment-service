package com.payment.service.controller;

import com.payment.service.dto.response.AddressTypeResponse;
import com.payment.service.service.AddressTypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/addressTypes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressTypeController extends BaseCatalogController<AddressTypeResponse, AddressTypeService> {
    AddressTypeService addressTypeService;

    @Override
    protected AddressTypeService getService() {
        return addressTypeService;
    }
}
