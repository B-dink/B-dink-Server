package com.app.bdink.payment.toss.entity;

import com.app.bdink.member.entity.Member;
import com.app.bdink.payment.toss.controller.dto.PaymentResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "essential_payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EssentialPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 200, updatable = false)
    private String paymentKey;

    @Column(length = 64, updatable = false)
    private String orderId;

    private Integer totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String version;

    private String mId;

    private String requestedAt;

    private String approvedAt;

    @Column(length = 64)
    private String lastTransactionKey;

    public static EssentialPayment from(Member member, PaymentResponse response) {
        EssentialPayment payment = new EssentialPayment();
        payment.setPaymentKey(response.paymentKey());
        payment.setOrderId(response.orderId());
        payment.setTotalAmount(response.totalAmount());
        payment.setVersion(response.version());
        payment.setMId(response.mId());
        payment.setRequestedAt(response.requestedAt());
        payment.setApprovedAt(response.approvedAt());
        payment.setLastTransactionKey(response.lastTransactionKey());
        payment.setMember(member);
        return payment;
    }
}
