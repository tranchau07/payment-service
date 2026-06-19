package com.payment.service.repository;

import com.payment.service.entity.Sic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SicRepository extends JpaRepository<Sic, Long>, JpaSpecificationExecutor<Sic> {
    Optional<Sic> findByCodeAndAmndState(String code, String amndState);
}
