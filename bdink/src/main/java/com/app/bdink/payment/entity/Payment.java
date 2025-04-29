package com.app.bdink.payment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 20)
    private String version;

    @Column(length = 20)
    private String type;

    @Column(length = 64)
    private String orderId;

    @Column(length = 100)
    private String orderName;

    @Column(length = 14)
    private String mId;

    @Column(length = 10)
    private String currency;

    @Column(length = 30)
    private String method;

    @Column(precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 19, scale = 2)
    private BigDecimal balanceAmount;

    @Column(length = 20)
    private String status;

    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;

    @Column(columnDefinition = "boolean default false")
    private boolean useEscrow;

    @Column(length = 64)
    private String lastTransactionKey;

    @Column(precision = 19, scale = 2)
    private BigDecimal suppliedAmount;

    @Column(precision = 19, scale = 2)
    private BigDecimal vat;

    @Column(columnDefinition = "boolean default false")
    private boolean cultureExpense;

    @Column(precision = 19, scale = 2)
    private BigDecimal taxFreeAmount;

    @Column(precision = 19, scale = 2)
    private BigDecimal taxExemptionAmount;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentCancel> cancels;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_info_id")
    private CardInfo cardInfo;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "virtual_account_id")
//    private VirtualAccount virtualAccount;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "mobile_phone_id")
//    private MobilePhone mobilePhone;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "gift_certificate_id")
//    private GiftCertificate giftCertificate;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "transfer_id")
//    private Transfer transfer;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "easy_pay_id")
//    private EasyPay easyPay;
//
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cash_receipt_id")
    private CashReceipt cashReceipt;
}
