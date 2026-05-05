package com.payment.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplProductResponse {
    Long id;
    String name;
    String code;
    String code2;
    String code3;
    String internalCode;
    Long fi;
    String ccat;
    Long clientType;
    String pcat;
    String conCat;
    Long contrType;
    Long contrSubtype;
    String baseRelation;
    String checkAvailable;
    String checkUsage;
    String liabCategory;
    Long accScheme;
    BigDecimal maxCreditLimit;
    BigDecimal minCreditLimit;
    BigDecimal defaultCreditLimit;
    String reportType;
    Long servPackType;
    Long servicePack;
    Long scoringModel;
    Long tariffDomain;
    Long tariffDomainTemplate;
    Long copyFromProduct;
    Long baseProduct;
    String customData;
    String isActive;
    Date dateFrom;
    Date dateTo;
    Integer nContracts;
    Long applProductOid;
    String parentCode;
    Long reportingProduct;
    Long mainProduct;
    Long applPrGroupId;
    String productGroup;
    String productStatus;
    String contractRole;
    String icon;
    Long productTemplate;
    Long dateScheme;
    String isReady;
    String amndState;
    Date amndDate;
}
