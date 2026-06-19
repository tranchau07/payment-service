package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "SIC")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sic_seq")
    @SequenceGenerator(name = "sic_seq", sequenceName = "SIC_SEQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name = "NAME", length = 255)
    String name;

    @Column(name = "CODE", length = 4)
    String code;

    @Column(name = "GROUP_CODE", length = 32)
    String groupCode;

    @Column(name = "CUSTOM_CODE", length = 32)
    String customCode;

    @Column(name = "LIMIT_CODE", length = 32)
    String limitCode;

    @Column(name = "SIC_GROUP_DFLT")
    Long sicGroupDflt;

    @Column(name = "USE_IN_BANK", length = 1)
    String useInBank;
}
