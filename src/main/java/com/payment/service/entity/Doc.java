package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "DOC", schema = "INT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Doc extends BaseEntity {

    @Id
    @Column(name = "ID")
    Long id;

    @Column(name = "SOURCE_NUMBER", length = 64)
    String sourceNumber;

    @Column(name = "TARGET_NUMBER", length = 64)
    String targetNumber;

    @Column(name = "TRANS_AMOUNT", precision = 28, scale = 10)
    BigDecimal transAmount;

    @Column(name = "TRANS_CURR", length = 3)
    String transCurr;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TRANS_DATE")
    Date transDate;

    @Column(name = "TRANS_DETAILS", length = 255)
    String transDetails;

    @Column(name = "AUTH_CODE", length = 32)
    String authCode;

    @Column(name = "POSTING_STATUS", length = 1)
    String postingStatus;

    @Column(name = "SOURCE_CONTRACT")
    Long sourceContract;

    @Column(name = "TARGET_CONTRACT")
    Long targetContract;

    @Column(name = "TRANS_TYPE")
    Long transType;

    @Column(name = "ADD_INFO", length = 3900)
    String addInfo;

    @Column(name = "COMMENT_TEXT", length = 3900)
    String commentText;

    @Column(name = "REASON_DETAILS", length = 3900)
    String reasonDetails;

    @Column(name = "RETURN_CODE")
    Integer returnCode;

    @Column(name = "DOC__ORIG__ID")
    Long docOrigId;

    @Column(name = "DOC__PREV__ID")
    Long docPrevId;

    @Column(name = "DOC__SUMM__ID")
    Long docSummId;

    @Column(name = "DOC__CHAIN__ID")
    Long docChainId;

    @Column(name = "NUMBER_OF_SUB_S")
    Integer numberOfSubS;

    @Column(name = "NUMBER_IN_CHAIN")
    Integer numberInChain;

    @Column(name = "ACTION", length = 32)
    String action;

    @Column(name = "MESSAGE_CATEGORY", length = 1)
    String messageCategory;

    @Column(name = "SOURCE_REG_NUM", length = 32)
    String sourceRegNum;

    @Column(name = "ACQ_REF_NUMBER", length = 32)
    String acqRefNumber;

    @Column(name = "RET_REF_NUMBER", length = 32)
    String retRefNumber;

    @Column(name = "ISS_REF_NUMBER", length = 32)
    String issRefNumber;

    @Column(name = "PS_REF_NUMBER", length = 32)
    String psRefNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NW_REF_DATE")
    Date nwRefDate;

    @Column(name = "IS_AUTHORIZATION", length = 1)
    String isAuthorization;

    @Column(name = "REQUEST_CATEGORY", length = 1)
    String requestCategory;

    @Column(name = "SERVICE_CLASS", length = 1)
    String serviceClass;

    @Column(name = "SOURCE_CODE", length = 32)
    String sourceCode;

    @Column(name = "SOURCE_FEE_CODE", length = 32)
    String sourceFeeCode;

    @Column(name = "TARGET_CODE", length = 32)
    String targetCode;

    @Column(name = "TARGET_FEE_CODE", length = 32)
    String targetFeeCode;

    @Column(name = "SOURCE_CHANNEL", length = 32)
    String sourceChannel;

    @Column(name = "S_CAT", length = 1)
    String sCat;

    @Column(name = "SOURCE_IDT_SCHEME", length = 32)
    String sourceIdtScheme;

    @Column(name = "SOURCE_MEMBER_ID", length = 32)
    String sourceMemberId;

    @Column(name = "REC_MEMBER_ID", length = 32)
    String recMemberId;

    @Column(name = "SOURCE_SPC", length = 255)
    String sourceSpc;

    @Column(name = "SOURCE_ACC_TYPE", length = 32)
    String sourceAccType;

    @Column(name = "SOURCE_SERVICE")
    Long sourceService;

    @Column(name = "TARGET_CHANNEL", length = 32)
    String targetChannel;

    @Column(name = "T_CAT", length = 1)
    String tCat;

    @Column(name = "TARGET_IDT_SCHEME", length = 32)
    String targetIdtScheme;

    @Column(name = "TARGET_MEMBER_ID", length = 32)
    String targetMemberId;

    @Column(name = "SEND_MEMBER_ID", length = 32)
    String sendMemberId;

    @Column(name = "SENDING_BIN", length = 32)
    String sendingBin;

    @Column(name = "TARGET_BIN_ID")
    Long targetBinId;

    @Column(name = "TARGET_SPC", length = 255)
    String targetSpc;

    @Column(name = "TARGET_ACC_TYPE", length = 32)
    String targetAccType;

    @Column(name = "TARGET_SERVICE")
    Long targetService;

    @Column(name = "TARGET_COUNTRY", length = 3)
    String targetCountry;

    @Column(name = "CARD_EXPIRE", length = 4)
    String cardExpire;

    @Column(name = "CARD_SEQV_NUMBER", length = 32)
    String cardSeqvNumber;

    @Column(name = "MERCHANT_ID", length = 32)
    String merchantId;

    @Column(name = "SIC_CODE", length = 4)
    String sicCode;

    @Column(name = "TRANS_CONDITION", length = 32)
    String transCondition;

    @Column(name = "TRANS_COND_ATTR")
    Long transCondAttr;

    @Column(name = "SEC_TRANS_COND_ATT")
    Long secTransCondAtt;

    @Column(name = "RECONS_CURR", length = 3)
    String reconsCurr;

    @Column(name = "RECONS_AMOUNT", precision = 28, scale = 10)
    BigDecimal reconsAmount;

    @Column(name = "SETTL_CURR", length = 3)
    String settlCurr;

    @Column(name = "SETTL_AMOUNT", precision = 28, scale = 10)
    BigDecimal settlAmount;

    @Column(name = "SOURCE_FEE_CURR", length = 3)
    String sourceFeeCurr;

    @Column(name = "SOURCE_FEE_AMOUNT", precision = 28, scale = 10)
    BigDecimal sourceFeeAmount;

    @Column(name = "TARGET_FEE_CURR", length = 3)
    String targetFeeCurr;

    @Column(name = "TARGET_FEE_AMOUNT", precision = 28, scale = 10)
    BigDecimal targetFeeAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SEC_TRANS_DATE")
    Date secTransDate;

    @Column(name = "TRANS_COUNTRY", length = 3)
    String transCountry;

    @Column(name = "TRANS_STATE", length = 32)
    String transState;

    @Column(name = "TRANS_CITY", length = 32)
    String transCity;

    @Column(name = "BIN_RECORD")
    Long binRecord;

    @Column(name = "REASON_CODE", length = 32)
    String reasonCode;

    @Column(name = "REQUIREMENT", length = 4)
    String requirement;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "POSTING_DATE")
    Date postingDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FX_SETTL_DATE")
    Date fxSettlDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REC_DATE")
    Date recDate;

    @Column(name = "OUTWARD_STATUS", length = 1)
    String outwardStatus;

    @Column(name = "CHANGE_VERSION")
    Integer changeVersion;

    @Column(name = "PARTITION_KEY", length = 32)
    String partitionKey;

    @Column(name = "SYNCH_TAG", length = 1)
    String synchTag;
}
