package com.payment.service.repository;

import com.payment.service.entity.AcntContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcntContractRepository extends JpaRepository<AcntContract, Long> {

    Optional<AcntContract> findByContractNumber(String contractNumber);
    Optional<AcntContract> findByContractNumberAndAmndState(String contractNumber, String amndState);
    Optional<AcntContract> findByMerchantIdAndAmndState(String merchantId, String amndState);
    Optional<AcntContract> findByRbsNumberAndAmndState(String rbsNumber, String amndState);

    List<AcntContract> findByClientIdAndContractLevel(Long clientId, String contractLevel);
    List<AcntContract> findByClientIdAndContractLevelAndAmndState(Long clientId, String contractLevel, String amndState);
    List<AcntContract> findByClientId(Long clientId);
    List<AcntContract> findByClientIdAndAmndState(Long clientId, String amndState);

    @Query("""
        SELECT c.product, COUNT(c)
        FROM AcntContract c
        WHERE c.amndState = :amndState
        GROUP BY c.product
    """)
    List<Object[]> countContractsGroupedByProduct(@Param("amndState") String amndState);

    interface ContractAddressProjection {
        String getAddressLine1();
        String getCity();
        String getCountry();
    }

    @Query(value = """
        SELECT
            ADDRESS_LINE_1 AS "addressLine1",
            CITY AS "city",
            COUNTRY AS "country"
        FROM CLIENT_ADDRESS
        WHERE ACNT_CONTRACT__OID = :contractOid
          AND AMND_STATE = 'A'
          AND IS_ACTIVE = 'Y'
          AND ROWNUM = 1
    """, nativeQuery = true)
    Optional<ContractAddressProjection> findContractAddressFields(@Param("contractOid") Long contractOid);
}