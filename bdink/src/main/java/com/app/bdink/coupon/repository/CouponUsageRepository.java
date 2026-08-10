package com.app.bdink.coupon.repository;

import com.app.bdink.coupon.entity.Coupon;
import com.app.bdink.coupon.entity.CouponUsage;
import com.app.bdink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {
    int countByCouponAndMember(Coupon coupon, Member member);
    boolean existsByCouponAndOrderId(Coupon coupon, String orderId);
}
