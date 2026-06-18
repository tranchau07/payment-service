package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "F_I")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Fi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "f_i_seq")
    @SequenceGenerator(name = "f_i_seq", sequenceName = "F_I_SEQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name = "NAME", length = 255)
    String name;

    @Column(name = "BANK_CODE", length = 32)
    String bankCode;

    @Column(name = "BRANCH_CODE", length = 32)
    String branchCode;

    @Column(name = "CB_CODE", length = 32)
    String cbCode;

    @Column(name = "UNIT")
    Long unit;

    @Column(name = "BANK_CLIENT")
    Long bankClient;

    @Column(name = "COUNTRY", length = 3)
    String country;

    @Column(name = "LOCAL_CURRENCY", length = 3)
    String localCurrency;

    @Column(name = "POSTING_IN")
    Long postingIn;

    @Column(name = "PARENT_FI")
    Long parentFi;

    @Column(name = "CALENDAR_TYPE")
    Long calendarType;

    @Column(name = "EXPENCE_PERCENT", precision = 15, scale = 8, nullable = false)
    BigDecimal expensePercent;

    @Column(name = "MIN_DEPOSIT", precision = 28, scale = 10, nullable = false)
    BigDecimal minDeposit;

    @Column(name = "FX_IN_HO", length = 1)
    String fxInHo;

    @Column(name = "PRNT_ROUTING", length = 1)
    String prntRouting;

    @Column(name = "CR_LIM_POSTING", length = 1)
    String crLimPosting;

    @Column(name = "POST_DUE", length = 1)
    String postDue;

    @Column(name = "INTEREST_IN_CYCLE", length = 1)
    String interestInCycle;

    @Column(name = "DAYS_IN_YEAR", precision = 3, nullable = false)
    Integer daysInYear;

    @Column(name = "MIRROR_SCHEME", length = 1)
    String mirrorScheme;

    @Column(name = "NUMERATION_SCHEME", length = 32)
    String numerationScheme;

    @Column(name = "SPECIAL_PARMS", length = 3900)
    String specialParms;

    @Column(name = "LIAB_CONTRACT")
    Long liabContract;

    @Column(name = "DEPOSIT_CONTRACT")
    Long depositContract;

    @Column(name = "SUNDRY_CONTRACT")
    Long sundryContract;

    @Column(name = "TARIFF_DOMAIN")
    Long tariffDomain;

    @Column(name = "TIME_ZONE", precision = 9, nullable = false)
    Long timeZone;

    @Column(name = "UNIT_TYPE", length = 32)
    String unitType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXT_LOCAL_DATE")
    Date extLocalDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOCAL_DATE")
    Date localDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_SCAN")
    Date lastScan;

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        if (expensePercent == null) {
            expensePercent = BigDecimal.ZERO;
        }
        if (minDeposit == null) {
            minDeposit = BigDecimal.ZERO;
        }
        if (daysInYear == null) {
            daysInYear = 0;
        }
        if (timeZone == null) {
            timeZone = 0L;
        }
    }
}
