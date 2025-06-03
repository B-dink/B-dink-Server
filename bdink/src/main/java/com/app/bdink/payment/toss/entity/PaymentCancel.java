package com.app.bdink.payment.toss.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "payment_cancel")
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

    @Column(precision = 19, scale = 0)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Builder
    public PaymentCancel(BigDecimal cancelAmount, String cancelReason, BigDecimal taxFreeAmount,
                         BigDecimal taxExemptionAmount, BigDecimal refundableAmount,
                         BigDecimal transferDiscountAmount, BigDecimal easyPayDiscountAmount,
                         LocalDateTime canceledAt, String transactionKey, String receiptKey,
                         String cancelStatus, String cancelRequestId) {
        this.cancelAmount = cancelAmount;
        this.cancelReason = cancelReason;
        this.taxFreeAmount = taxFreeAmount;
        this.taxExemptionAmount = taxExemptionAmount;
        this.refundableAmount = refundableAmount;
        this.transferDiscountAmount = transferDiscountAmount;
        this.easyPayDiscountAmount = easyPayDiscountAmount;
        this.canceledAt = canceledAt;
        this.transactionKey = transactionKey;
        this.receiptKey = receiptKey;
        this.cancelStatus = cancelStatus;
        this.cancelRequestId = cancelRequestId;
    }
}