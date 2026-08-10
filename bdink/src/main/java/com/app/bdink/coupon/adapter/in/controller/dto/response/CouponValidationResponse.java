package com.app.bdink.coupon.adapter.in.controller.dto.response;

import com.app.bdink.coupon.entity.Coupon;

public record CouponValidationResponse(
        String code,
        int discountRate,
        int discountedPrice
) {
    public static CouponValidationResponse of(Coupon coupon, int discountedPrice) {
        return new CouponValidationResponse(coupon.getCode(), coupon.getDiscountRate(), discountedPrice);
    }
}
