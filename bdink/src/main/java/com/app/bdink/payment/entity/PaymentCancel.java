package com.app.bdink.payment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "payment_cancels")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentCancel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(precision = 19, scale = 2)
    private BigDecimal cancelAmount;

    @Column(length = 200)
    private String cancelReason;

    @Column(precision = 19, scale = 2)
    private BigDecimal taxFreeAmount;

    @Column(precision = 19, scale = 2)
    private BigDecimal taxExemptionAmount;

    @Column(precision = 19, scale = 2)
    private BigDecimal refundableAmount;

    @Column(precision = 19, scale = 2)
    private BigDecimal transferDiscountAmount;

    @Column(precision = 19, scale = 2)
    private BigDecimal easyPayDiscountAmount;

    private LocalDateTime canceledAt;

    @Column(length = 64)
    private String transactionKey;

    @Column(length = 200)
    private String receiptKey;

    @Column(length = 20)
    private String cancelStatus;

    @Column(length = 64)
    private String cancelRequestId;

    @ManyToOne
    @JoinColumn(name = "payment_key")
    private Payment payment;
}