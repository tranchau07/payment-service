package com.payment.service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCardRequest {
    String contractIdentifier;
    String productCode;
    CardInObject inObject;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CardInObject {
        String cardName;
        String branch;
        String cbsNumber;
        String embossedFirstName;
        String embossedLastName;
        String embossedCompanyName;
    }
}
