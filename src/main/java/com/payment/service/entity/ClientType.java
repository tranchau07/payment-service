package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "CLIENT_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_type_seq")
    @SequenceGenerator(name = "client_type_seq", sequenceName = "INT.CLIENT_TYPE_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    Long id;

    @Column(name = "CODE", length = 32)
    String code;

    @Column(name = "NAME", length = 255)
    String name;

    @Column(name = "F_I")
    Long financialInstitutionId;

    @Column(name = "PCAT", length = 1)
    String productCategory;

    @Column(name = "CCAT", length = 1)
    String clientCategory;

    @Column(name = "RESIDENCE", length = 1)
    String residence;

}