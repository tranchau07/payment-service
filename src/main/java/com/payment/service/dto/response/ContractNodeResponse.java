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
public class ContractNodeResponse {
    Long id;
    String contractNumber;
    String contractName;
    String contractLevel;
    String productCode;
    String productName;
    String productType; // "LIABILITY", "ISSUING", "CARD", "UNKNOWN"
    
    // Parent links
    Long parentId;
    Long liabContract;
    Long acntContractOid;
    Long billingContract;
    
    String curr;
    BigDecimal amountAvailable;
    BigDecimal totalBalance;
    Date dateOpen;
    Date dateExpire;

    String addressLine1;
    String city;
    String country;
}
