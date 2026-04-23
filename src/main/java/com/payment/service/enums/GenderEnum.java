package com.payment.service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum GenderEnum {

    MALE("M", "Male"),
    FEMALE("F", "Female"),
    NOT_SPECIFIED("N", "Not specified");

    private final String code;
    private final String display;

    GenderEnum(String code, String display) {
        this.code = code;
        this.display = display;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static GenderEnum fromCode(String code) {
        for (GenderEnum g : values()) {
            if (g.code.equalsIgnoreCase(code)) {
                return g;
            }
        }
        throw new IllegalArgumentException("Invalid Gender: " + code);
    }
}