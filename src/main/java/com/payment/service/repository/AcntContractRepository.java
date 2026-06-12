package com.payment.service.repository;

import com.payment.service.entity.AcntContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcntContractRepository extends JpaRepository<AcntContract, Long> {
    Optional<AcntContract> findByContractNumber(String contractNumber);
    Optional<AcntContract> findByContractNumberAndAmndState(String contractNumber, String amndState);
    List<AcntContract> findByClientIdAndContractLevel(Long clientId, String contractLevel);
    List<AcntContract> findByClientIdAndContractLevelAndAmndState(Long clientId, String contractLevel, String amndState);
    List<AcntContract> findByClientId(Long clientId);
    List<AcntContract> findByClientIdAndAmndState(Long clientId, String amndState);
}
