package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "CURRENCY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Currency extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_seq")
    @SequenceGenerator(name = "currency_seq", sequenceName = "CURRENCY_SEQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name = "NAME", length = 255)
    String name;

    @Column(name = "CODE", length = 3)
    String code;

    @Column(name = "N_CODE", length = 3)
    String numericCode;

    @Column(name = "IS_ACTIVE", length = 1)
    String isActive;
}
