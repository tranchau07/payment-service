package com.payment.service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractResponse {
    Long id;
    String contractNumber;
    Long clientId;
    String product;
    String curr;
    String conCat;
    String contractName;
    String branch;
    BigDecimal amountAvailable;
    BigDecimal totalBalance;
    Date dateOpen;
    Date dateExpire;
}
