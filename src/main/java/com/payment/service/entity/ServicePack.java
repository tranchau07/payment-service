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
@Table(name = "SERV_PACK")
@Getter
@NoArgsConstructor
public class ServicePack extends BaseEntity {
    @Id @Column(name = "ID") Long id;
    @Column(name = "CODE") String code;
    @Column(name = "NAME") String name;
    @Column(name = "CCAT") String clientCategory;
    @Column(name = "CON_CAT") String contractCategory;
    @Column(name = "CONTR_TYPE__OID") Long contractTypeId;
    @Column(name = "SERV_PACK_TYPE__ID") Long servicePackTypeId;
    @Column(name = "BASE_PACK") Long basePackId;
    @Column(name = "PARENT_PACK") Long parentPackId;
    @Column(name = "USE_FOR_CONTRACTS") String useForContracts;
    @Column(name = "IS_READY") String isReady;
}
