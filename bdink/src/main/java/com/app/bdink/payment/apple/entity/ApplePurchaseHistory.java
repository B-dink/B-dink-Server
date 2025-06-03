package com.app.bdink.payment.apple.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplePurchaseHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    public static ApplePurchaseHistory createPurchase(
            Long userId, String productId, String transactionId) {
        return ApplePurchaseHistory.builder()
                .userId(userId)
                .productId(productId)
                .transactionId(transactionId)
                .build();
    }
}
