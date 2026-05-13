package com.payment.service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateIssuingContractWithLiabilityRequest {
    String liabContractIdentifier;
    String clientIdentifier;
    String productCode;
    String productCode2;
    String productCode3;
    String branch;
    String institutionCode;
    String contractName;
    String cbsNumber;
    String addInfo01;
    String addInfo02;
}
