package com.payment.service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ProductCategoryEnum {

    ACQUIRING("M", "Acquiring"),
    ACCOUNTING("A", "Accounting"),
    ISSUING("C", "Issuing"),
    BANK_ACCOUNTING("B", "Bank Accounting");

    private final String code;
    private final String display;

    ProductCategoryEnum(String code, String display) {
        this.code = code;
        this.display = display;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static ProductCategoryEnum fromCode(String code) {
        for (ProductCategoryEnum p : values()) {
            if (p.code.equalsIgnoreCase(code)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid ProductCategory: " + code);
    }
}