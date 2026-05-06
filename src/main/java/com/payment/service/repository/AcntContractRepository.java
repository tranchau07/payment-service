package com.payment.service.repository;

import com.payment.service.entity.AcntContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcntContractRepository extends JpaRepository<AcntContract, Long> {
    Optional<AcntContract> findByContractNumber(String contractNumber);
}
