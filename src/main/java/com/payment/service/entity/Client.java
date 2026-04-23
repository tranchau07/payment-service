package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "CLIENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    @SequenceGenerator(name = "client_seq", sequenceName = "CLIENT_SEQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name = "F_I")
    Long parentId;

    @Column(name = "BRANCH", length = 32)
    String branchCode;

    @Column(name = "CCAT", length = 1)
    String clientCategory;

    @Column(name = "CLT")
    Long clientType;

    @Column(name = "PCAT", length = 1)
    String productCategory;

    @Column(name = "SERVICE_GROUP", length = 32)
    String serviceGroup;

    @Column(name = "SHORT_NAME", length = 255)
    String shortName;

    @Column(name = "TITLE")
    Long title;

    @Column(name = "SALUTATION_SUFFIX", length = 32)
    String salutationSuffix;

    @Column(name = "FIRST_NAM", length = 255)
    String firstName;

    @Column(name = "LAST_NAM", length = 255)
    String lastName;

    @Column(name = "FATHER_S_NAM", length = 255)
    String fatherSurname;

    @Column(name = "BIRTH_NAM", length = 255)
    String birthName;

    @Column(name = "MOTHER_S_NAM", length = 255)
    String motherSurname;

    @Column(name = "BIRTH_DATE")
    LocalDate birthDate;

    @Column(name = "BIRTH_PLACE", length = 255)
    String birthPlace;

    @Column(name = "GENDER", length = 1)
    String gender;

    @Column(name = "ENABLE_AFFILIATION", length = 1)
    String enableAffiliation;

    @Column(name = "AFFILIATION_TYPE", length = 32)
    String affiliationType;

    @Column(name = "AFFILIATED_WITH")
    Long affiliatedWith;

    @Column(name = "MARITAL_STATUS")
    Long maritalStatus;

    @Column(name = "COMPANY_NAM", length = 255)
    String companyName;

    @Column(name = "TRADE_NAM", length = 255)
    String tradeName;

    @Column(name = "COMPANY_DEPARTMENT", length = 255)
    String companyDepartment;

    @Column(name = "CLIENT_NUMBER", length = 64)
    String clientNumber;

    @Column(name = "REG_NUMBER_TYPE", length = 32)
    String regNumberType;

    @Column(name = "REG_NUMBER", length = 64)
    String regNumber;

    @Column(name = "REG_DETAILS", length = 255)
    String regDetails;

    @Column(name = "ITN", length = 64)
    String itn;

    @Column(name = "SOCIAL_NUMBER", length = 64)
    String socialNumber;

    @Column(name = "TAX_POSITION", length = 32)
    String taxPosition;

    @Column(name = "PROFESSION", length = 255)
    String profession;

    @Column(name = "LANGUAGE")
    Long language;

    @Column(name = "CITIZENSHIP", length = 3)
    String citizenship;

    @Column(name = "DATE_EXPIRE")
    LocalDate dateExpire;

    @Column(name = "DATE_OPEN")
    LocalDate dateOpen;

    @Column(name = "TR_TITLE")
    Long trTitle;

    @Column(name = "TR_FIRST_NAM", length = 32)
    String trFirstName;

    @Column(name = "TR_LAST_NAM", length = 32)
    String trLastName;

    @Column(name = "COUNTRY", length = 3)
    String country;

    @Column(name = "ADDRESS_ZIP", length = 32)
    String addressZip;

    @Column(name = "STATE", length = 32)
    String state;

    @Column(name = "CITY", length = 255)
    String city;

    @Column(name = "ADDRESS_LINE_1", length = 255)
    String addressLine1;

    @Column(name = "ADDRESS_LINE_2", length = 255)
    String addressLine2;

    @Column(name = "ADDRESS_LINE_3", length = 255)
    String addressLine3;

    @Column(name = "ADDRESS_LINE_4", length = 255)
    String addressLine4;

    @Column(name = "PHONE", length = 32)
    String phone;

    @Column(name = "PHONE_H", length = 32)
    String phoneHome;

    @Column(name = "PHONE_M", length = 32)
    String phoneMobile;

    @Column(name = "FAX", length = 32)
    String fax;

    @Column(name = "FAX_H", length = 32)
    String faxHome;

    @Column(name = "E_MAIL", length = 255)
    String email;

    @Column(name = "URL", length = 255)
    String url;

    @Column(name = "DELIVERY_TYPE")
    Long deliveryType;

    @Column(name = "TR_COMPANY_NAM", length = 32)
    String trCompanyName;

    @Column(name = "ADD_DATE_01")
    LocalDate addDate01;

    @Column(name = "ADD_DATE_02")
    LocalDate addDate02;

    @Column(name = "ADD_INFO_01", length = 3900)
    String addInfo01;

    @Column(name = "ADD_INFO_02", length = 3900)
    String addInfo02;

    @Column(name = "ADD_INFO_03", length = 3900)
    String addInfo03;

    @Column(name = "ADD_INFO_04", length = 3900)
    String addInfo04;

    @Column(name = "IS_READY", length = 1)
    String isReady;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_I", insertable = false, updatable = false)
    Client previousVersion;
}