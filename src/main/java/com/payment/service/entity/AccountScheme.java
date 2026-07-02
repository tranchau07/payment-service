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
@Table(name = "ACC_SCHEME")
@Getter
@NoArgsConstructor
public class AccountScheme extends BaseEntity {
    @Id @Column(name = "ID") Long id;
    @Column(name = "CODE") String code;
    @Column(name = "SCHEME_NAME") String name;
    @Column(name = "PCAT") String productCategory;
    @Column(name = "CCAT") String clientCategory;
    @Column(name = "CURR") String currency;
    @Column(name = "USE_FOR_CONTRACTS") String useForContracts;
    @Column(name = "PARENT_SCHEME") Long parentSchemeId;
    @Column(name = "IS_READY") String isReady;
}
