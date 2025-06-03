package com.app.bdink.payment.toss.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "gift_certificate")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 8)
    private String approveNo;

    @Column(length = 20)
    private String settlementStatus; // INCOMPLETED, COMPLETED

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Builder
    public GiftCertificate(String approveNo, String settlementStatus) {
        this.approveNo = approveNo;
        this.settlementStatus = settlementStatus;
    }
}