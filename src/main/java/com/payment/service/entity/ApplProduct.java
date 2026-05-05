package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "APPL_PRODUCT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appl_product_seq")
    @SequenceGenerator(name = "appl_product_seq", sequenceName = "APPL_PRODUCT_SEQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name = "NAME", length = 255)
    String name;

    @Column(name = "CODE", length = 32)
    String code;

    @Column(name = "CODE_2", length = 32)
    String code2;

    @Column(name = "CODE_3", length = 32)
    String code3;

    @Column(name = "INTERNAL_CODE", length = 32)
    String internalCode;

    @Column(name = "F_I")
    Long fi;

    @Column(name = "CCAT", length = 1)
    String ccat;

    @Column(name = "CLIENT_TYPE")
    Long clientType;

    @Column(name = "PCAT", length = 1)
    String pcat;

    @Column(name = "CON_CAT", length = 1)
    String conCat;

    @Column(name = "CONTR_TYPE")
    Long contrType;

    @Column(name = "CONTR_SUBTYPE")
    Long contrSubtype;

    @Column(name = "BASE_RELATION", length = 255)
    String baseRelation;

    @Column(name = "CHECK_AVAILABLE", length = 1)
    String checkAvailable;

    @Column(name = "CHECK_USAGE", length = 1)
    String checkUsage;

    @Column(name = "LIAB_CATEGORY", length = 1)
    String liabCategory;

    @Column(name = "ACC_SCHEME")
    Long accScheme;

    @Column(name = "MAX_CREDIT_LIMIT")
    BigDecimal maxCreditLimit;

    @Column(name = "MIN_CREDIT_LIMIT")
    BigDecimal minCreditLimit;

    @Column(name = "DEFAULT_CREDIT_LIMIT")
    BigDecimal defaultCreditLimit;

    @Column(name = "REPORT_TYPE", length = 32)
    String reportType;

    @Column(name = "SERV_PACK_TYPE")
    Long servPackType;

    @Column(name = "SERVICE_PACK")
    Long servicePack;

    @Column(name = "SCORING_MODEL")
    Long scoringModel;

    @Column(name = "TARIFF_DOMAIN")
    Long tariffDomain;

    @Column(name = "TARIFF_DOMAIN_TEMPLATE")
    Long tariffDomainTemplate;

    @Column(name = "COPY_FROM_PRODUCT")
    Long copyFromProduct;

    @Column(name = "BASE_PRODUCT")
    Long baseProduct;

    @Column(name = "CUSTOM_DATA", length = 4000)
    String customData;

    @Column(name = "IS_ACTIVE", length = 1)
    String isActive;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_FROM")
    Date dateFrom;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_TO")
    Date dateTo;

    @Column(name = "N_CONTRACTS")
    Integer nContracts;

    @Column(name = "APPL_PRODUCT__OID")
    Long applProductOid;

    @Column(name = "PARENT_CODE", length = 32)
    String parentCode;

    @Column(name = "REPORTING_PRODUCT")
    Long reportingProduct;

    @Column(name = "MAIN_PRODUCT")
    Long mainProduct;

    @Column(name = "APPL_PR_GROUP__ID")
    Long applPrGroupId;

    @Column(name = "PRODUCT_GROUP", length = 32)
    String productGroup;

    @Column(name = "PRODUCT_STATUS", length = 1)
    String productStatus;

    @Column(name = "CONTRACT_ROLE", length = 32)
    String contractRole;

    @Column(name = "ICON", length = 255)
    String icon;

    @Column(name = "PRODUCT_TEMPLATE")
    Long productTemplate;

    @Column(name = "DATE_SCHEME")
    Long dateScheme;

    @Column(name = "IS_READY", length = 1)
    String isReady;
}
