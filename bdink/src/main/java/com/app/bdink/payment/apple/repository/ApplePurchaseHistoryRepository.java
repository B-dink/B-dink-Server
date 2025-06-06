package com.app.bdink.payment.apple.repository;

import com.app.bdink.payment.apple.entity.ApplePurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplePurchaseHistoryRepository extends JpaRepository<ApplePurchaseHistory, Long> {

    List<ApplePurchaseHistory> findAllByUserId(Long memberId);
    boolean existsByUserIdAndOriginalTransactionIdAndProductId(Long memberId, String originalTransactionId, String productId);
    Optional<ApplePurchaseHistory> findByUserIdAndOriginalTransactionIdAndProductId(Long memberId, String originalTransactionId, String productId);
}
