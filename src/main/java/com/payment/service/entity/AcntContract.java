package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "ACNT_CONTRACT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AcntContract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acnt_contract_seq")
    @SequenceGenerator(name = "acnt_contract_seq", sequenceName = "ACNT_CONTRACT_SEQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name = "PCAT", length = 1)
    String pcat;

    @Column(name = "CON_CAT", length = 1)
    String conCat;

    @Column(name = "TERMINAL_CATEGORY", length = 1)
    String terminalCategory;

    @Column(name = "CCAT", length = 1)
    String ccat;

    @Column(name = "F_I")
    Long fi;

    @Column(name = "BRANCH", length = 32)
    String branch;

    @Column(name = "SERVICE_GROUP", length = 32)
    String serviceGroup;

    @Column(name = "CONTRACT_NUMBER", length = 64)
    String contractNumber;

    @Column(name = "BASE_RELATION", length = 255)
    String baseRelation;

    @Column(name = "CONTRACT_NAME", length = 255)
    String contractName;

    @Column(name = "COMMENT_TEXT", length = 255)
    String commentText;

    @Column(name = "RELATION_TAG", length = 1)
    String relationTag;

    @Column(name = "ACNT_CONTRACT__ID")
    Long acntContractId;

    @Column(name = "CONTR_TYPE")
    Long contrType;

    @Column(name = "CONTR_SUBTYPE__ID")
    Long contrSubtypeId;

    @Column(name = "SERV_PACK__ID")
    Long servPackId;

    @Column(name = "ACC_SCHEME__ID")
    Long accSchemeId;

    @Column(name = "OLD_PACK")
    Long oldPack;

    @Column(name = "CHANNEL", length = 32)
    String channel;

    @Column(name = "OLD_SCHEME")
    Long oldScheme;

    @Column(name = "PRODUCT", length = 32)
    String product;

    @Column(name = "PARENT_PRODUCT", length = 32)
    String parentProduct;

    @Column(name = "PRODUCT_PREV", length = 32)
    String productPrev;

    @Column(name = "MAIN_PRODUCT")
    Long mainProduct;

    @Column(name = "LIAB_CATEGORY", length = 1)
    String liabCategory;

    @Column(name = "CLIENT__ID")
    Long clientId;

    @Column(name = "CLIENT_TYPE")
    Long clientType;

    @Column(name = "ACNT_CONTRACT__OID")
    Long acntContractOid;

    @Column(name = "LIAB_CONTRACT")
    Long liabContract;

    @Column(name = "LIAB_CONTRACT_PREV")
    Long liabContractPrev;

    @Column(name = "BILLING_CONTRACT")
    Long billingContract;

    @Column(name = "BEHAVIOR_GROUP")
    Long behaviorGroup;

    @Column(name = "BEHAVIOR_TYPE")
    Long behaviorType;

    @Column(name = "BEHAVIOR_TYPE_PREV")
    Long behaviorTypePrev;

    @Column(name = "CHECK_AVAILABLE", length = 1)
    String checkAvailable;

    @Column(name = "CHECK_USAGE", length = 1)
    String checkUsage;

    @Column(name = "CURR", length = 3)
    String curr;

    @Column(name = "OLD_CURR", length = 3)
    String oldCurr;

    @Column(name = "AUTH_LIMIT_AMOUNT", precision = 28, scale = 10)
    BigDecimal authLimitAmount;

    @Column(name = "BASE_AUTH_LIMIT", precision = 28, scale = 10)
    BigDecimal baseAuthLimit;

    @Column(name = "LIAB_BALANCE", precision = 28, scale = 10)
    BigDecimal liabBalance;

    @Column(name = "LIAB_BLOCKED", precision = 28, scale = 10)
    BigDecimal liabBlocked;

    @Column(name = "OWN_BALANCE", precision = 28, scale = 10)
    BigDecimal ownBalance;

    @Column(name = "OWN_BLOCKED", precision = 28, scale = 10)
    BigDecimal ownBlocked;

    @Column(name = "SUB_BALANCE", precision = 28, scale = 10)
    BigDecimal subBalance;

    @Column(name = "SUB_BLOCKED", precision = 28, scale = 10)
    BigDecimal subBlocked;

    @Column(name = "TOTAL_BLOCKED", precision = 28, scale = 10)
    BigDecimal totalBlocked;

    @Column(name = "TOTAL_BALANCE", precision = 28, scale = 10)
    BigDecimal totalBalance;

    @Column(name = "SHARED_BALANCE", precision = 28, scale = 10)
    BigDecimal sharedBalance;

    @Column(name = "SHARED_BLOCKED", precision = 28, scale = 10)
    BigDecimal sharedBlocked;

    @Column(name = "AMOUNT_AVAILABLE", precision = 28, scale = 10)
    BigDecimal amountAvailable;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_OPEN")
    Date dateOpen;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_EXPIRE")
    Date dateExpire;

    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_BILLING_DATE")
    Date lastBillingDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "NEXT_BILLING_DATE")
    Date nextBillingDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_SCAN")
    Date lastScan;

    @Column(name = "CARD_EXPIRE", length = 4)
    String cardExpire;

    @Column(name = "PRODUCTION_STATUS", length = 1)
    String productionStatus;

    @Column(name = "RBS_MEMBER_ID", length = 32)
    String rbsMemberId;

    @Column(name = "RBS_NUMBER", length = 64)
    String rbsNumber;

    @Column(name = "REPORT_TYPE", length = 32)
    String reportType;

    @Column(name = "MAX_PIN_ATTEMPTS")
    Integer maxPinAttempts;

    @Column(name = "PIN_ATTEMPTS")
    Integer pinAttempts;

    @Column(name = "RISK_SCHEME")
    Long riskScheme;

    @Column(name = "CHIP_SCHEME")
    Long chipScheme;

    @Column(name = "RISK_FACTOR", precision = 7, scale = 3)
    BigDecimal riskFactor;

    @Column(name = "RISK_FACTOR_PREV", precision = 7, scale = 3)
    BigDecimal riskFactorPrev;

    @Column(name = "CONTR_STATUS")
    Long contrStatus;

    @Column(name = "MERCHANT_ID", length = 255)
    String merchantId;

    @Column(name = "TR_TITLE")
    Long trTitle;

    @Column(name = "TR_COMPANY", length = 32)
    String trCompany;

    @Column(name = "TR_COUNTRY", length = 3)
    String trCountry;

    @Column(name = "TR_FIRST_NAM", length = 32)
    String trFirstName;

    @Column(name = "TR_LAST_NAM", length = 32)
    String trLastName;

    @Column(name = "TR_SIC", length = 4)
    String trSic;

    @Column(name = "ADD_INFO_01", length = 255)
    String addInfo01;

    @Column(name = "ADD_INFO_02", length = 255)
    String addInfo02;

    @Column(name = "ADD_INFO_03", length = 255)
    String addInfo03;

    @Column(name = "ADD_INFO_04", length = 255)
    String addInfo04;

    @Column(name = "CONTRACT_LEVEL", length = 255)
    String contractLevel;

    @Column(name = "EXT_DATA", length = 4000)
    String extData;

    @Column(name = "REPORT_ADDRESS")
    Long reportAddress;

    @Column(name = "SHARE_BALANCE", length = 1)
    String shareBalance;

    @Column(name = "IS_MULTYCURRENCY", length = 1)
    String isMulticurrency;

    @Column(name = "ENABLES_ITEM", length = 1)
    String enablesItem;

    @Column(name = "CYCLE_LENGTH")
    Integer cycleLength;

    @Column(name = "INTERVAL_TYPE", length = 1)
    String intervalType;

    @Column(name = "STATUS_CATEGORY", length = 1)
    String statusCategory;

    @Column(name = "LIMIT_IS_ACTIVE", length = 1)
    String limitIsActive;

    @Column(name = "ROUTING_IDT", length = 32)
    String routingIdt;

    @Column(name = "IS_READY", length = 1)
    String isReady;

    @Column(name = "SETTLEMENT_TYPE", length = 1)
    String settlementType;

    @Column(name = "AUTH_SEQ_N")
    Long authSeqN;

    @Temporal(TemporalType.DATE)
    @Column(name = "APPLY_DT")
    Date applyDt;

    @Column(name = "LOCAL_VERSION")
    Integer localVersion;

    @Column(name = "REMOTE_VERSION")
    Integer remoteVersion;
}
