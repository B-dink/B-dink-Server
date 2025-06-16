package com.app.bdink.payment.apple.repository;

import com.app.bdink.payment.apple.dto.AppleProductResponse;
import com.app.bdink.payment.apple.entity.AppleProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppleProductRepository extends JpaRepository<AppleProduct, Long> {
    Optional<AppleProduct> findByProductId(String productId);

    Optional<AppleProduct> findByClassRoomId(Long classRoomId);
}
