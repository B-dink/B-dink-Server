package com.app.bdink.payment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "card_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(length = 10)
    private String issuerCode;

    @Column(length = 10)
    private String acquirerCode;

    @Column(length = 20)
    private String number;

    private Integer installmentPlanMonths;

    @Column(length = 10)
    private String approveNo;

    @Column(columnDefinition = "boolean default false")
    private boolean useCardPoint;

    @Column(length = 20)
    private String cardType;

    @Column(length = 20)
    private String ownerType;

    @Column(length = 20)
    private String acquireStatus;

    @Column(columnDefinition = "boolean default false")
    private boolean isInterestFree;

    @Column(length = 20)
    private String interestPayer;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}