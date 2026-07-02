package com.payment.service.repository;

import com.payment.service.entity.ServicePack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ServicePackRepository extends JpaRepository<ServicePack, Long> {
    List<ServicePack> findAllByIdInAndAmndState(Collection<Long> ids, String amndState);
}
