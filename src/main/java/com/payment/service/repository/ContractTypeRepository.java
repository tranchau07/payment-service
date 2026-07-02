package com.payment.service.repository;

import com.payment.service.entity.ContractType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ContractTypeRepository extends JpaRepository<ContractType, Long> {
    List<ContractType> findAllByIdInAndAmndState(Collection<Long> ids, String amndState);
}
