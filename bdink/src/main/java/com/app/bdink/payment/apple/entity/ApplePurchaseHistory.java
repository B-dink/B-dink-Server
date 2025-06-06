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
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_user_original_transaction_product",
                columnNames = {"user_id", "original_transaction_id", "product_id"}
        )
})
public class ApplePurchaseHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "original_transaction_id", nullable = false)
    private String originalTransactionId;

    public static ApplePurchaseHistory createPurchase(
            Long userId, String productId, String transactionId, String originalTransactionId) {
        return ApplePurchaseHistory.builder()
                .userId(userId)
                .productId(productId)
                .transactionId(transactionId)
                .originalTransactionId(originalTransactionId)
                .build();
    }
}
