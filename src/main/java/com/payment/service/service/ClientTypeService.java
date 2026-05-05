package com.payment.service.service;

import com.payment.service.dto.response.ClientTypeResponse;
import com.payment.service.mapper.ClientTypeMapper;
import com.payment.service.repository.ClientTypeRepository;
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
public class ClientTypeService {
    ClientTypeRepository clientTypeRepository;
    ClientTypeMapper clientTypeMapper;

    public List<ClientTypeResponse> getAllClientType() {
        return clientTypeMapper.toClientTypeResponses(clientTypeRepository.findAll());
    }
}
