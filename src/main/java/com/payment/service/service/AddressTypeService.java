package com.payment.service.service;

import com.payment.service.dto.response.AddressTypeResponse;
import com.payment.service.mapper.AddressTypeMapper;
import com.payment.service.repository.AddressTypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class AddressTypeService {
    AddressTypeRepository addressTypeRepository;
    AddressTypeMapper addressTypeMapper;

    public AddressTypeResponse getAddressTypeByCode(String code) {
        log.info("Getting by code: {}", code);
        return addressTypeMapper.toAddressTypeResponse(addressTypeRepository.findByCode(code).orElseThrow(RuntimeException::new
                ));
    }

    public List<AddressTypeResponse> getAllSa() {
        return addressTypeMapper.toAddressTypeResponses(addressTypeRepository.findAll());
    }
}
