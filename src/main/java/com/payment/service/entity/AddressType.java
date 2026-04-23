package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "ADDRESS_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_type_seq")
    @SequenceGenerator(name = "address_type_seq", sequenceName = "ADDRESS_TYPE_SEQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AMND_PREV", insertable = false, updatable = false)
    AddressType previousVersion;

    @Column(name = "NAME", length = 255)
    String name; // Tên hiển thị (VD: Residential Address)

    @Column(name = "CODE", length = 32)
    String code; // Code dùng trong API (VD: RESIDENTIAL)

    @Column(name = "GROUP_CODE", length = 32)
    String groupCode; // Nhóm địa chỉ (optional)

    @Column(name = "STANDBY_ADDR_TYPE")
    Long standbyAddressType; // ID của address type fallback

    @Column(name = "USE_CLIENT_DEFAULT", length = 1)
    String useClientDefault; // Y/N

    @Column(name = "ADD_INFO", length = 3900)
    String addInfo; // custom info
}