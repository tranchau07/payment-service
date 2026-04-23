package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "COUNTRY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Country extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "country_seq")
    @SequenceGenerator(name = "country_seq", sequenceName = "COUNTRY_SEQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AMND_PREV", insertable = false, updatable = false)
    Country previousVersion;

    // ===== BUSINESS =====
    @Column(name = "NAME", length = 255)
    String name;

    @Column(name = "CODE", length = 3)
    String code;        // ISO alpha-3 (VNM, USA)

    @Column(name = "CODE_2", length = 2)
    String code2;       // ISO alpha-2 (VN, US)

    @Column(name = "N_CODE", length = 3)
    String numericCode; // ISO numeric (704, 840)

    @Column(name = "CURR_CODE", length = 3)
    String currencyCode;

    @Column(name = "CURR_NAME", length = 32)
    String currencyName;

    @Column(name = "CUSTOM_CODE", length = 32)
    String customCode;

    @Column(name = "POSTAL_CODE", length = 32)
    String postalCode;

    @Column(name = "USE_IN_BANK", length = 1)
    String useInBank;

    @Column(name = "DEFAULT_LANGUAGE")
    Long defaultLanguageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEFAULT_LANGUAGE", insertable = false, updatable = false)
    Lang defaultLanguage;

    @Column(name = "AREA_DFLT")
    Long areaDefault;

    @Column(name = "LIMIT_CODE", length = 32)
    String limitCode;

    @Column(name = "COUNTRY_OBJECT__ID")
    Long countryObjectId;

    @Column(name = "CALENDAR_TYPE")
    Long calendarType;

    @Column(name = "N_CURR_CODE", length = 3)
    String numericCurrencyCode;
}