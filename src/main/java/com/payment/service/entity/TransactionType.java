package com.payment.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "TRANS_TYPE", schema = "INT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionType extends BaseEntity {
    @Id
    @Column(name = "ID")
    Long id;

    @Column(name = "NAME", length = 255)
    String name;

    @Column(name = "SERVICE_CLASS", length = 1)
    String serviceClass;

    @Column(name = "S_CAT", length = 1)
    String sourceCategory;

    @Column(name = "T_CAT", length = 1)
    String targetCategory;

    @Column(name = "DR_CR")
    Integer debitCredit;

    @Column(name = "TRANS_TYPE_IDT", length = 32)
    String identityCode;
}
