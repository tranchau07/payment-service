package com.payment.service.service;

import com.payment.service.dto.response.BranchResponse;
import com.payment.service.entity.Branch;
import com.payment.service.mapper.BranchMapper;
import com.payment.service.repository.BaseRepository;
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
public class BranchService extends BaseCatalogService<Branch, BranchResponse> {
    BranchRepository branchRepository;
    BranchMapper branchMapper;

    @Override
    protected BaseRepository<Branch, Long> getRepository() {
        return branchRepository;
    }

    @Override
    protected BranchResponse toResponse(Branch entity) {
        return branchMapper.toBranchResponse(entity);
    }

    @Override
    protected String getEntityName() {
        return "Branch";
    }

    public BranchResponse getBranchByCode(String code) {
        return getByCode(code);
    }

    public List<BranchResponse> getAllSa() {
        return getAll();
    }
}
