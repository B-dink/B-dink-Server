package com.app.bdink.payment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "mobile_phone")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MobilePhone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 15)
    private String customerMobilePhone;

    @Column(length = 20)
    private String settlementStatus; // INCOMPLETED, COMPLETED

    @Column(length = 255)
    private String receiptUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Builder
    public MobilePhone(String customerMobilePhone, String settlementStatus, String receiptUrl) {
        this.customerMobilePhone = customerMobilePhone;
        this.settlementStatus = settlementStatus;
        this.receiptUrl = receiptUrl;
    }
}