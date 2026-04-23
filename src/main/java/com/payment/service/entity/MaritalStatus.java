package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "MARITAL_STATUS")
@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaritalStatus extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "marital_status_seq")
    @SequenceGenerator(name = "marital_status_seq", sequenceName = "MARITAL_STATUS_SEQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AMND_PREV", insertable = false, updatable = false)
    MaritalStatus previousVersion;

    @Column(name = "CODE", length = 32)
    String code;

    @Column(name = "NAME", length = 255)
    String name;
}