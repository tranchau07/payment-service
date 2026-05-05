package com.payment.service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientSearchRequest {
    String branchCode;
    String shortName;
    String clientNumber;
    String phoneNumber;
    String itn;
}
