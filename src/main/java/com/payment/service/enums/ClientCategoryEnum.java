package com.payment.service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ClientCategoryEnum {

    PRIVATE("P", "Private"),
    COMMERCIAL("C", "Commercial"),
    ACCOUNTANT("A", "Accountant");

    private final String code;
    private final String display;

    ClientCategoryEnum(String code, String display) {
        this.code = code;
        this.display = display;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static ClientCategoryEnum fromCode(String code) {
        for (ClientCategoryEnum c : values()) {
            if (c.code.equalsIgnoreCase(code)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid ClientCategory: " + code);
    }
}