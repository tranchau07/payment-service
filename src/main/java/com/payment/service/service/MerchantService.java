package com.payment.service.service;

import com.payment.service.dto.request.MerchantSearchRequest;
import com.payment.service.dto.response.ClientListResponse;
import com.payment.service.entity.Client;
import com.payment.service.mapper.ClientMapper;
import com.payment.service.repository.ClientRepository;
import com.payment.service.repository.specification.MerchantSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MerchantService {

    ClientRepository clientRepository;
    ClientMapper clientMapper;

    @Transactional(readOnly = true)
    public ClientListResponse searchMerchants(MerchantSearchRequest searchRequest, Pageable pageable) {
        log.info("Searching merchants with criteria: {}, pageable: {}", searchRequest, pageable);

        Specification<Client> spec = MerchantSpecification.buildSearch(searchRequest);
        Page<Client> clientPage = clientRepository.findAll(spec, pageable);

        log.info("Found {} merchants", clientPage.getTotalElements());
        return clientMapper.toListResponse(clientPage);
    }
}
