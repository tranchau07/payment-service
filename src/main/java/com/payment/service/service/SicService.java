package com.payment.service.service;

import com.payment.service.dto.request.SicSearchRequest;
import com.payment.service.dto.response.SicResponse;
import com.payment.service.mapper.SicMapper;
import com.payment.service.repository.SicRepository;
import com.payment.service.repository.specification.SicSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SicService {

    SicRepository sicRepository;
    SicMapper sicMapper;

    public List<SicResponse> searchSics(SicSearchRequest request) {
        log.info("Fetching Sics with search criteria: {}", request);
        Specification<com.payment.service.entity.Sic> spec = SicSpecification.buildSearch(request);
        return sicMapper.toResponses(sicRepository.findAll(spec));
    }
}
