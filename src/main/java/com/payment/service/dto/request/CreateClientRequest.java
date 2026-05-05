package com.payment.service.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateClientRequest {

    String reason;
    ClientInfo clientInfo;
    AddressInfo addressInfo;
    List<CustomData> customData;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AddressInfo {
        String addressType;
        String country;
        String city;
        String addressZip;
        String state;
        String addressLine1;
        String addressLine2;
        String addressLine3;
        String addressLine4;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ClientInfo {
        String institutionCode;
        String branchCode;
        String clientTypeCode;
        String shortName;
        String firstName;
        String lastName;
        String middleName;
        String maritalStatusCode;
        String socialNumber;
        String salutationCode;
        String birthDate;
        String gender;
        String citizenship;
        String individualTaxpayerNumber;
        String companyName;
        String identityCardNumber;
        String identityCardDetails;
        String clientNumber;
        String profession;
        String email;
        String homePhone;
        String mobilePhone;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CustomData {
        String addInfoType;
        String tagName;
        String tagValue;
    }
}