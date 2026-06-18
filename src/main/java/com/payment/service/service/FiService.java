package com.payment.service.service;

import com.payment.service.dto.response.FiResponse;
import com.payment.service.entity.Fi;
import com.payment.service.mapper.FiMapper;
import com.payment.service.repository.FiRepository;
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
public class FiService {
    FiRepository fiRepository;
    FiMapper fiMapper;

    public List<FiResponse> getAll() {
        log.info("Getting all Financial Institutions (F_I)");
        List<Fi> fiList = fiRepository.findAll();
        return fiMapper.toFiResponses(fiList);
    }
}
