package com.payment.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateIssuingContractWithLiabilityResponse {
    String createdContract;
    String contractNumber;
    String applicationNumber;
    Long retCode;
    String retMsg;
    String resultInfo;

    public boolean isSuccess() {
        return retCode != null && retCode == 0;
    }
}
