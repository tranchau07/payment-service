package com.payment.service.repository;

import com.payment.service.entity.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressTypeRepository extends BaseRepository<AddressType, Long> {
}