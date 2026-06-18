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
public class FiResponse {
    Long id;
    String name;
    String bankCode;
    String branchCode;
    String cbCode;
    Long unit;
    Long bankClient;
    String country;
    String localCurrency;
    Long postingIn;
    Long parentFi;
    Long calendarType;
    BigDecimal expensePercent;
    BigDecimal minDeposit;
    String fxInHo;
    String prntRouting;
    String crLimPosting;
    String postDue;
    String interestInCycle;
    Integer daysInYear;
    String mirrorScheme;
    String numerationScheme;
    String specialParms;
    Long liabContract;
    Long depositContract;
    Long sundryContract;
    Long tariffDomain;
    Long timeZone;
    String unitType;
    Date extLocalDate;
    Date localDate;
    Date lastScan;
}
