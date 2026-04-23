package com.payment.service.repository;

import com.payment.service.entity.Lang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LangRepository extends JpaRepository<Lang, Long> {
    Optional<Lang> findByCode(String code);
}