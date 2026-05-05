package com.payment.service.service;

import com.payment.service.dto.response.BranchResponse;
import com.payment.service.mapper.BranchMapper;
import com.payment.service.repository.BranchRepository;
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
public class BranchService {
    BranchRepository branchRepository;
    BranchMapper branchMapper;

    public BranchResponse getBranchByCode(String code) {
        log.info("Getting by code: {}", code);
        return branchMapper.toBranchResponse(branchRepository.findByCode(code).orElseThrow(RuntimeException::new
                ));
    }

    public List<BranchResponse> getAllSa() {
        return branchMapper.toBranchResponses(branchRepository.findAll());
    }
}
