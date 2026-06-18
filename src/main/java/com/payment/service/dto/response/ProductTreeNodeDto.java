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
    Long contrSubtype;
    Long accScheme;
    Long servicePack;
    BigDecimal maxCreditLimit;
    BigDecimal minCreditLimit;
    BigDecimal defaultCreditLimit;
    Integer ncontracts;
    String isActive;
    List<ProductTreeNodeDto> children;
}
