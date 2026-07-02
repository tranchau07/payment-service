package com.payment.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractCashFlowResponse {
    Long contractId;
    String contractNumber;
    String contractName;
    String product;
    String contractLevel;
    String balanceCurrency;
    String currency;
    BigDecimal amountAvailable;
    BigDecimal totalBalance;
    BigDecimal totalBlocked;
    BigDecimal totalReceived;
    BigDecimal totalSent;
    BigDecimal netCashFlow;
    Long transactionCount;
    Date firstTransactionDate;
    Date lastTransactionDate;
}
