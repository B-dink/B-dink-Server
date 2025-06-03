package com.app.bdink.payment.apple;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppleProductRepository extends JpaRepository<AppleProduct, Long> {
    Optional<AppleProduct> findByProductId(String productId);
}
