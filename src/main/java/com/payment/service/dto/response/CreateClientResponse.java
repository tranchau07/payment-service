package com.payment.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateClientResponse {

    Long newClientId;              // NewClient
    String applicationNumber;      // ApplicationNumber

    Long retCode;                  // 0 = success (thường vậy)
    String retMsg;

    String debugInfo;
    String resultInfo;

    public boolean isSuccess() {
        return retCode != null && retCode == 0;
    }
}