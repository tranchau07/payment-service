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
public class DocResponse {
    Long id;
    String sourceNumber;
    String targetNumber;
    BigDecimal transAmount;
    String transCurr;
    String transCurrName;
    Date transDate;
    String transDetails;
    String authCode;

    String postingStatus;

    Long sourceContract;
    Long targetContract;
    Long transType;
    String transTypeName;
    String transTypeIdt;
    Integer debitCredit;

    String addInfo;
    String commentText;
    String reasonDetails;
    Integer returnCode;

    String amndState;
    Date amndDate;
    Long amndOfficer;
    Long amndPrev;

    Long docOrigId;
    Long docPrevId;
    Long docSummId;
    Long docChainId;
    Integer numberOfSubS;
    Integer numberInChain;
    String action;

    String messageCategory;

    String sourceRegNum;
    String acqRefNumber;
    String retRefNumber;
    String issRefNumber;
    String psRefNumber;
    Date nwRefDate;

    String isAuthorization;

    String requestCategory;

    String serviceClass;

    String sourceCode;
    String sourceFeeCode;
    String targetCode;
    String targetFeeCode;
    String sourceChannel;

    String sCat;

    String sourceIdtScheme;
    String sourceMemberId;
    String recMemberId;
    String sourceSpc;
    String sourceAccType;
    Long sourceService;

    String targetChannel;
    String tCat;

    String targetIdtScheme;
    String targetMemberId;
    String sendMemberId;
    String sendingBin;
    Long targetBinId;
    String targetSpc;
    String targetAccType;
    Long targetService;
    String targetCountry;

    String cardExpire;
    String cardSeqvNumber;
    String merchantId;
    String sicCode;
    String transCondition;
    Long transCondAttr;
    Long secTransCondAtt;

    String reconsCurr;
    BigDecimal reconsAmount;
    String settlCurr;
    BigDecimal settlAmount;
    String sourceFeeCurr;
    BigDecimal sourceFeeAmount;
    String targetFeeCurr;
    BigDecimal targetFeeAmount;

    Date secTransDate;
    String transCountry;
    String transState;
    String transCity;
    Long binRecord;
    String reasonCode;
    String requirement;
    Date postingDate;
    Date fxSettlDate;
    Date recDate;

    String outwardStatus;


    Integer changeVersion;
    String partitionKey;
    String synchTag;
}
