package com.app.bdink.payment.apple.repository;

import com.app.bdink.payment.apple.entity.ApplePurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplePurchaseHistoryRepository extends JpaRepository<ApplePurchaseHistory, Long> {

    Boolean existsByTransactionId(String transactionId);
    List<ApplePurchaseHistory> findAllByUserId(Long memberId);
}
