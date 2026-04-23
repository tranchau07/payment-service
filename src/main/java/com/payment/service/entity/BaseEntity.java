package com.payment.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity implements java.io.Serializable {
    @Column(name = "AMND_STATE", length = 1)
    private String amndState;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AMND_DATE")
    private Date amndDate;

    @Column(name = "AMND_OFFICER", precision = 18)
    private Long amndOfficer;

    @Column(name = "AMND_PREV", precision = 18)
    private Long amndPrev;

    @PrePersist
    protected void onCreate() {
        if (amndState == null) {
            amndState = "A"; // Active
        }
        if (amndDate == null) {
            amndDate = new Date(); // tương đương sysdate
        }
    }

    @PreUpdate
    protected void onUpdate() {
        amndDate = new Date(); // update mỗi lần sửa
    }
}
