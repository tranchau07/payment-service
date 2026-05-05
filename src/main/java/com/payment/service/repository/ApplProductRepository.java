package com.payment.service.repository;

import com.payment.service.entity.ApplProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplProductRepository extends JpaRepository<ApplProduct, Long> {
    Optional<ApplProduct> findByCode(String code);
}
