package com.payment.service.mapper;

import com.payment.service.dto.response.BranchResponse;
import com.payment.service.entity.Branch;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    BranchResponse toBranchResponse(Branch bra);
    List<BranchResponse> toBranchResponses(List<Branch> braList);
}
