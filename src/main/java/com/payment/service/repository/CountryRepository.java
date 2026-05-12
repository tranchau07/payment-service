package com.payment.service.repository;

import com.payment.service.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends BaseRepository<Country, Long> {
}