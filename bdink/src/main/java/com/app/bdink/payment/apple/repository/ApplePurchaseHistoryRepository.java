package com.app.bdink.payment.apple.repository;

import com.app.bdink.payment.apple.entity.ApplePurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplePurchaseHistoryRepository extends JpaRepository<ApplePurchaseHistory, Long> {
    Boolean existsByTransactionId(String transactionId);
}
