package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "CLIENT_TITLE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientTitle extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_title_seq")
    @SequenceGenerator(name = "client_title_seq", sequenceName = "CLIENT_TITLE_SEQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AMND_PREV", insertable = false, updatable = false)
    ClientTitle previousVersion;

    @Column(name = "CODE", length = 32)
    String code;

    @Column(name = "NAME", length = 255)
    String name;
}
