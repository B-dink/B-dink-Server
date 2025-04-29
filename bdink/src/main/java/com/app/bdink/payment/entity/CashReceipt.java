package com.app.bdink.payment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CashReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String receiptKey;

    private String orderId;

    private String orderName;

    private String type;

    private String issueNumber;

    private String receiptUrl;

    private String businessNumber;

    private Integer amount;

    private Integer taxFreeAmount;

    private String issueStatus;

    private Object failure;

    private String customerIdentityNumber;

    private String requestedAt;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

}