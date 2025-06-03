package com.app.bdink.payment.toss.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "transfers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 10)
    private String bankCode;

    @Column(length = 20)
    private String settlementStatus; // INCOMPLETED, COMPLETED

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Builder
    public Transfer(String bankCode, String settlementStatus) {
        this.bankCode = bankCode;
        this.settlementStatus = settlementStatus;
    }
}