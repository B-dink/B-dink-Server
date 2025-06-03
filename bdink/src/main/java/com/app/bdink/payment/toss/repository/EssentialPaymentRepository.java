package com.app.bdink.payment.toss.repository;

import com.app.bdink.payment.toss.entity.EssentialPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EssentialPaymentRepository extends JpaRepository<EssentialPayment, Long> {
}
