package com.payment.service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateIssuingContractWithLiabilityRequest {

    String liabCategory;
    String liabContractSearchMethod;
    String liabContractIdentifier;

    String clientSearchMethod;
    String clientIdentifier;

    String productIdentifier;
    String applRegNumber;

    InObject inObject;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class InObject {
        String branch;
        String institutionCode;
        String currency;
        String contractName;
        String addInfo01;
        String addInfo02;
    }
}
