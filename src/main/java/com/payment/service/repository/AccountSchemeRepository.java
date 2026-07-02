package com.payment.service.repository;

import com.payment.service.entity.AccountScheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AccountSchemeRepository extends JpaRepository<AccountScheme, Long> {
    List<AccountScheme> findAllByIdInAndAmndState(Collection<Long> ids, String amndState);
}
