package com.payment.service.repository;

import com.payment.service.entity.ApplProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplProductRepository extends JpaRepository<ApplProduct, Long> {
    Optional<ApplProduct> findByCode(String code);
    Optional<ApplProduct> findByInternalCode(String code);
    List<ApplProduct> findAllByIsReadyAndAmndState(String isReady, String amndState);
    List<ApplProduct> findAllByAmndState(String amndState);
    List<ApplProduct> findAllByParentCode(String parentCode);
    List<ApplProduct> findAllByPcatAndAmndStateAndIsReadyOrderByNameAsc(String pcat, String amndState, String isReady);
    List<ApplProduct> findAllByPcatAndAmndStateOrderByNameAsc(String pcat, String amndState);
}
