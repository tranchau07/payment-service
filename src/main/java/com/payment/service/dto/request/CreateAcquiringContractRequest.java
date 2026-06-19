package com.payment.service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateAcquiringContractRequest {
    String clientSearchMethod;
    String clientIdentifier;
    String productCode;
    String productCode2;
    String productCode3;
    InObject inObject;
    AddressObject address;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class InObject {
        String institutionCode;
        String branch;
        String contractNumber;
        String contractName;
        String currency;
        String mcc;
        String merchantId;
        String cbsNumber;
        String openDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AddressObject {
        String addressTypeCode;
        String branch;
        String country;
        String region;
        String district;
        String city;
        String zipCode;
        String line1;
        String line2;
        String line3;
        String line4;
    }
}
