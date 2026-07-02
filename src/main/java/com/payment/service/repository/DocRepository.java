package com.payment.service.repository;

import com.payment.service.entity.Doc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.math.BigDecimal;

@Repository
public interface DocRepository extends JpaRepository<Doc, Long> {

    interface ContractCashFlowProjection {
        Long getContractId();
        String getContractNumber();
        String getContractName();
        String getProduct();
        String getContractLevel();
        String getBalanceCurrency();
        String getCurrency();
        BigDecimal getAmountAvailable();
        BigDecimal getTotalBalance();
        BigDecimal getTotalBlocked();
        BigDecimal getTotalReceived();
        BigDecimal getTotalSent();
        Long getTransactionCount();
        Date getFirstTransactionDate();
        Date getLastTransactionDate();
    }

    @Query(value = """
        WITH movements AS (
            SELECT d.ID doc_id, d.SOURCE_CONTRACT contract_id,
                   NVL(d.TRANS_CURR, c.CURR) currency,
                   CASE WHEN d.TRANS_AMOUNT < 0 THEN ABS(d.TRANS_AMOUNT) ELSE 0 END amount_in,
                   CASE WHEN d.TRANS_AMOUNT > 0 THEN d.TRANS_AMOUNT ELSE 0 END amount_out,
                   d.TRANS_DATE trans_date
            FROM INT.DOC d
            JOIN INT.ACNT_CONTRACT c ON c.ID = d.SOURCE_CONTRACT AND c.AMND_STATE = 'A'
            WHERE d.AMND_STATE = 'A' AND d.POSTING_STATUS = 'P'
              AND d.TRANS_AMOUNT <> 0 AND d.SOURCE_CONTRACT IS NOT NULL
            UNION ALL
            SELECT d.ID doc_id, d.TARGET_CONTRACT contract_id,
                   NVL(d.TRANS_CURR, c.CURR) currency,
                   CASE WHEN d.TRANS_AMOUNT > 0 THEN d.TRANS_AMOUNT ELSE 0 END amount_in,
                   CASE WHEN d.TRANS_AMOUNT < 0 THEN ABS(d.TRANS_AMOUNT) ELSE 0 END amount_out,
                   d.TRANS_DATE trans_date
            FROM INT.DOC d
            JOIN INT.ACNT_CONTRACT c ON c.ID = d.TARGET_CONTRACT AND c.AMND_STATE = 'A'
            WHERE d.AMND_STATE = 'A' AND d.POSTING_STATUS = 'P'
              AND d.TRANS_AMOUNT <> 0 AND d.TARGET_CONTRACT IS NOT NULL
        )
        SELECT c.ID AS "contractId",
               c.CONTRACT_NUMBER AS "contractNumber",
               c.CONTRACT_NAME AS "contractName",
               c.PRODUCT AS "product",
               c.CONTRACT_LEVEL AS "contractLevel",
               c.CURR AS "balanceCurrency",
               m.currency AS "currency",
               c.AMOUNT_AVAILABLE AS "amountAvailable",
               c.TOTAL_BALANCE AS "totalBalance",
               c.TOTAL_BLOCKED AS "totalBlocked",
               SUM(m.amount_in) AS "totalReceived",
               SUM(m.amount_out) AS "totalSent",
               COUNT(DISTINCT m.doc_id) AS "transactionCount",
               MIN(m.trans_date) AS "firstTransactionDate",
               MAX(m.trans_date) AS "lastTransactionDate"
        FROM movements m
        JOIN INT.ACNT_CONTRACT c ON c.ID = m.contract_id AND c.AMND_STATE = 'A'
        GROUP BY c.ID, c.CONTRACT_NUMBER, c.CONTRACT_NAME, c.PRODUCT,
                 c.CONTRACT_LEVEL, c.CURR, m.currency, c.AMOUNT_AVAILABLE,
                 c.TOTAL_BALANCE, c.TOTAL_BLOCKED
        ORDER BY MAX(m.trans_date) DESC, c.ID
        """, nativeQuery = true)
    java.util.List<ContractCashFlowProjection> summarizePostedCashFlowByContract();
    
    Page<Doc> findAllByAmndState(String amndState, Pageable pageable);

    @Query("SELECT DISTINCT d.transType FROM Doc d WHERE d.amndState = 'A' AND d.transType IS NOT NULL")
    java.util.List<Long> findUsedTransactionTypeIds();

    @Query("SELECT DISTINCT d.postingStatus FROM Doc d WHERE d.amndState = 'A' AND d.postingStatus IS NOT NULL")
    java.util.List<String> findUsedPostingStatuses();

    @Query("SELECT DISTINCT d.isAuthorization FROM Doc d WHERE d.amndState = 'A' AND d.isAuthorization IS NOT NULL")
    java.util.List<String> findUsedDocumentCategories();

    @Query("SELECT DISTINCT d.requestCategory FROM Doc d WHERE d.amndState = 'A' AND d.requestCategory IS NOT NULL")
    java.util.List<String> findUsedRequestCategories();

    @Query("SELECT DISTINCT d.serviceClass FROM Doc d WHERE d.amndState = 'A' AND d.serviceClass IS NOT NULL")
    java.util.List<String> findUsedServiceClasses();
    
    @Query("SELECT d FROM Doc d WHERE (d.sourceContract = :contractId OR d.targetContract = :contractId) AND d.amndState = 'A'")
    Page<Doc> findByContractId(@Param("contractId") Long contractId, Pageable pageable);

    @Query("SELECT d FROM Doc d WHERE " +
           "(:contractId IS NULL OR d.sourceContract = :contractId OR d.targetContract = :contractId) AND " +
           "(:number IS NULL OR d.sourceNumber LIKE %:number% OR d.targetNumber LIKE %:number% OR d.authCode LIKE %:number%) AND " +
           "(:startDate IS NULL OR d.transDate >= :startDate) AND " +
           "(:endDate IS NULL OR d.transDate < :endDate) AND " +
           "(:transType IS NULL OR d.transType = :transType) AND " +
           "(:postingStatus IS NULL OR d.postingStatus = :postingStatus) AND " +
           "(:documentCategory IS NULL OR d.isAuthorization = :documentCategory) AND " +
           "(:requestCategory IS NULL OR d.requestCategory = :requestCategory) AND " +
           "(:serviceClass IS NULL OR d.serviceClass = :serviceClass) AND " +
           "(:returnCode IS NULL OR d.returnCode = :returnCode) AND " +
           "d.amndState = 'A'")
    Page<Doc> searchDocs(
            @Param("contractId") Long contractId,
            @Param("number") String number,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("transType") Long transType,
            @Param("postingStatus") String postingStatus,
            @Param("documentCategory") String documentCategory,
            @Param("requestCategory") String requestCategory,
            @Param("serviceClass") String serviceClass,
            @Param("returnCode") Integer returnCode,
            Pageable pageable);
}
