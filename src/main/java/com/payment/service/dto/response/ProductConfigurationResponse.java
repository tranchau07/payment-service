package com.payment.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductConfigurationResponse {
    private Long id;
    private String code;
    private String name;
    private String internalCode;
    private String businessType;
    private String pcat;
    private String conCat;
    private String ccat;
    private String parentCode;
    private boolean rootProduct;
    private String isActive;
    private String isReady;

    private ConfigurationReference contractType;
    private ConfigurationReference contractSubtype;
    private ConfigurationReference accountScheme;
    private ConfigurationReference servicePack;
    private List<String> missingReferences;

    private BigDecimal minCreditLimit;
    private BigDecimal maxCreditLimit;
    private BigDecimal defaultCreditLimit;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfigurationReference {
        private Long id;
        private String code;
        private String name;
        private String displayName;
        private String sourceTable;
        private Map<String, Object> attributes;
    }
}
