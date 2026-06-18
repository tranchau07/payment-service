package com.payment.service.repository;

import com.payment.service.entity.Fi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FiRepository extends JpaRepository<Fi, Long> {
}
