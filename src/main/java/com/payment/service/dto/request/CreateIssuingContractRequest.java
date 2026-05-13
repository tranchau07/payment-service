package com.payment.service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateIssuingContractRequest {
    String liabContractIdentifier;
    String clientIdentifier;
    String productCode;
    String branch;
    String institutionCode;
    String cbsNumber;
    String embossedFirstName;
    String embossedLastName;
    String addInfo01;
    String addInfo02;
}
