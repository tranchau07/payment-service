package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Table(name = "LANG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Lang extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lang_seq")
    @SequenceGenerator(name = "lang_seq", sequenceName = "LANG_SEQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AMND_PREV", insertable = false, updatable = false)
    Lang previousVersion;

    @Column(name = "CODE", length = 3)
    String code;        // ISO 639-2 (VD: VIE, ENG)

    @Column(name = "CODE_2", length = 2)
    String code2;       // ISO 639-1 (VD: vi, en)

    @Column(name = "COUNTRY_CODE2", length = 2)
    String countryCode2; // country (VD: VN, US)

    @Column(name = "NAME", length = 32)
    String name;        // Vietnamese, English,...
}