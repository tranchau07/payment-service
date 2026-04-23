package com.payment.service.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateClientRequest {
    private String reason;
    private ClientInfo clientInfo;
    private List<CustomData> customData;

    @Data
    public static class ClientInfo {
        private String branchCode;
        private String clientTypeCode;
        private String clientCategory;
        private String productCategory;
        private String shortName;
        private String firstName;
        private String lastName;
        private String middleName;
        private String birthDate;
        private String gender;
        private String citizenship;
        private String socialNumber;
        private String regNumber;
        private String regNumberType;
        private String profession;
        private String email;
        private String mobilePhone;
    }

    @Data
    public static class CustomData {
        private String tagName;
        private String tagValue;
    }
}