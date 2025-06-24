package com.app.bdink.payment.apple.repository;

import com.app.bdink.payment.apple.entity.ApplePurchaseHistory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplePurchaseHistoryRepository extends JpaRepository<ApplePurchaseHistory, Long> {

    List<ApplePurchaseHistory> findAllByUserId(Long memberId);

    @Lock(LockModeType.PESSIMISTIC_READ)
//    @Query("SELECT ph FROM ApplePurchaseHistory ph WHERE ph.productId = :productId AND ph.userId = :userId")
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM ApplePurchaseHistory p " +
            "WHERE p.userId = :userId AND p.productId = :productId")
    boolean findByUserIdAndProductIdWithLock(@Param("userId") Long userId, @Param("productId") String productId);

    Optional<ApplePurchaseHistory> findByUserIdAndOriginalTransactionIdAndProductId(Long memberId, String originalTransactionId, String productId);
}
