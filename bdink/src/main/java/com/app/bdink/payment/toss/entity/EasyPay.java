package com.app.bdink.payment.toss.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "easy_pays")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EasyPay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 30)
    private String provider;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(precision = 19, scale = 2)
    private BigDecimal discountAmount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Builder
    public EasyPay(String provider, BigDecimal amount, BigDecimal discountAmount) {
        this.provider = provider;
        this.amount = amount;
        this.discountAmount = discountAmount;
    }
}