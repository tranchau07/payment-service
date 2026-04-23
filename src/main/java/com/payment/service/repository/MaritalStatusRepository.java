package com.payment.service.repository;

import com.payment.service.entity.MaritalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaritalStatusRepository extends JpaRepository<MaritalStatus, Long> {
    Optional<MaritalStatus> findByCode(String code);
}