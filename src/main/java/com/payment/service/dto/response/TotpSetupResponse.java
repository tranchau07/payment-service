package com.payment.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TotpSetupResponse {
    String qrCodeBase64;
    String secret;
    String username;
}
