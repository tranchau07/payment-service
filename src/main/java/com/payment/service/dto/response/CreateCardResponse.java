package com.payment.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCardResponse {
    String cardNumber;
    String applicationNumber;
    Long retCode;
    String retMsg;
    String debugInfo;
    String resultInfo;

    public boolean isSuccess() {
        return retCode != null && retCode == 0;
    }
}
