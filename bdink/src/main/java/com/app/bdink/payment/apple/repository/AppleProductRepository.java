package com.app.bdink.payment.apple.repository;

import com.app.bdink.payment.apple.entity.AppleProduct;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppleProductRepository extends JpaRepository<AppleProduct, Long> {
    Optional<AppleProduct> findByProductId(String productId);

    Optional<AppleProduct> findByClassRoomId(Long classRoomId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM AppleProduct p WHERE p.productId = :productId")
    Optional<AppleProduct> findByProductIdWithLock(@Param("productId") String productId);
}
