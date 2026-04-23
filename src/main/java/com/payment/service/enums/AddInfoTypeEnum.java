package com.payment.service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum AddInfoTypeEnum {

    ADD_INFO_01("AddInfo01", "Add Info 01"),
    ADD_INFO_02("AddInfo02", "Add Info 02"),
    ADD_INFO_03("AddInfo03", "Add Info 03"),
    ADD_INFO_04("AddInfo04", "Add Info 04");

    private final String code;
    private final String display;

    AddInfoTypeEnum(String code, String display) {
        this.code = code;
        this.display = display;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static AddInfoTypeEnum fromCode(String code) {
        for (AddInfoTypeEnum e : values()) {
            if (e.code.equalsIgnoreCase(code)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Invalid AddInfoType: " + code);
    }
}