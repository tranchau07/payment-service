package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "AFFILIATION_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AffiliationType extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "affiliation_type_seq")
    @SequenceGenerator(name = "affiliation_type_seq", sequenceName = "AFFILIATION_TYPE_SEQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AMND_PREV", insertable = false, updatable = false)
    AffiliationType previousVersion;

    @Column(name = "CODE", length = 32)
    String code;

    @Column(name = "NAME", length = 255)
    String name;

    @Column(name = "AFFILIATION_OBJECT_TYPE", length = 1)
    String affiliationObjectType;

    @Column(name = "GROUP_CODE", length = 32)
    String groupCode;

    @Column(name = "RIGHTS_SET", length = 32)
    String rightsSet;

    @Column(name = "ADD_DATA", length = 4000)
    String addData;
}