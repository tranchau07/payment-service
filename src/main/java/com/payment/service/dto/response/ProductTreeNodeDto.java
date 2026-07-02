package com.payment.service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProductTreeNodeDto {
    Long id;
    String code;
    String name;
    String internalCode;
    String parentCode;
    String pcat;
    String conCat;
    Long contrType;
    String contrTypeDesc;
    Long contrSubtype;
    String contrSubtypeDesc;
    Long accScheme;
    String accSchemeDesc;
    Long servicePack;
    String servicePackDesc;
    BigDecimal maxCreditLimit;
    BigDecimal minCreditLimit;
    BigDecimal defaultCreditLimit;
    Integer ncontracts;
    String isActive;
    String isReady;
    List<ProductTreeNodeDto> children;
}
