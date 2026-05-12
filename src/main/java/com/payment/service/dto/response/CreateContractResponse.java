package com.payment.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateContractResponse {

    String contractNumber;        // Số hợp đồng mới (nếu có)
    String applicationNumber;

    Long retCode;                 // 0 = success
    String retMsg;

    String debugInfo;
    String resultInfo;

    public boolean isSuccess() {
        return retCode != null && retCode == 0;
    }
}
