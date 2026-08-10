package com.app.bdink.coupon.adapter.in.controller.dto.request;

public record CreateCouponRequest(
        String code,
        Long classRoomId,
        int discountRate,
        int maxUsagePerMember
) {
}
