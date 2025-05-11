package com.app.bdink.payment.repository;

import com.app.bdink.payment.entity.EssentialPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EssentialPaymentRepository extends JpaRepository<EssentialPayment, Long> {
}
