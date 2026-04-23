package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "BRANCH")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Branch extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_seq")
    @SequenceGenerator(name = "branch_seq", sequenceName = "BRANCH_SEQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name = "NAME", length = 255)
    String name;

    @Column(name = "CODE", length = 32)
    String code;

    @Column(name = "F_I")
    Long parentId;

    @Column(name = "BRANCH__OID")
    Long branchOid;

    @Column(name = "LIAB_CONTRACT")
    Long liabContract;

    @Column(name = "UNIT")
    Long unit;

    @Column(name = "BANK_CLIENT")
    Long bankClient;

    @Column(name = "TIME_ZONE")
    Long timeZone;

    @Column(name = "UNIT_TYPE", length = 32)
    String unitType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BRANCH__OID", insertable = false, updatable = false)
    Branch rootBranch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_I", insertable = false, updatable = false)
    Branch previousVersion;
}