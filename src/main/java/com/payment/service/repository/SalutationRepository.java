package com.payment.service.repository;

import com.payment.service.entity.Salutation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalutationRepository extends JpaRepository<Salutation, Long> {
    Optional<Salutation> findByCode(String code);
}