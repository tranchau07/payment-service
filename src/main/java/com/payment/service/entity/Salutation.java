package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "SALUTATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Salutation extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salutation_seq")
    @SequenceGenerator(name = "salutation_seq", sequenceName = "SALUTATION_SEQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AMND_PREV", insertable = false, updatable = false)
    Salutation previousVersion;

    @Column(name = "CODE", length = 32)
    String code;

    @Column(name = "NAME", length = 255)
    String name;
}