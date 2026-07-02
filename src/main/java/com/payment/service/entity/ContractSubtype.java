package com.payment.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "CONTR_SUBTYPE")
@Getter
@NoArgsConstructor
public class ContractSubtype extends BaseEntity {
    @Id @Column(name = "ID") Long id;
    @Column(name = "CONTR_TYPE__OID") Long contractTypeId;
    @Column(name = "CODE") String code;
    @Column(name = "NAME") String name;
    @Column(name = "CON_CAT") String contractCategory;
    @Column(name = "TERMINAL_CATEGORY") String terminalCategory;
    @Column(name = "CCAT") String clientCategory;
    @Column(name = "CHANNEL") String channel;
    @Column(name = "IS_ACTIVE") String isActive;
}
