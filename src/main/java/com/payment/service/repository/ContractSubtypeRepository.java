package com.payment.service.repository;

import com.payment.service.entity.ContractSubtype;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ContractSubtypeRepository extends JpaRepository<ContractSubtype, Long> {
    List<ContractSubtype> findAllByIdInAndAmndState(Collection<Long> ids, String amndState);
}
