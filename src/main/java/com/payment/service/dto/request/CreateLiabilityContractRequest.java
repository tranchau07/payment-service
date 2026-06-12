package com.payment.service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateLiabilityContractRequest {
    String clientNumber; // The unique client business number (CIF)
    String cbsNumber;    // CBS CASA account number entered by Teller
}
