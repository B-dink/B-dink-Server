package com.app.bdink.coupon.service;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.coupon.adapter.in.controller.dto.response.CouponValidationResponse;
import com.app.bdink.coupon.entity.Coupon;
import com.app.bdink.coupon.entity.CouponUsage;
import com.app.bdink.coupon.repository.CouponRepository;
import com.app.bdink.coupon.repository.CouponUsageRepository;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponUsageRepository couponUsageRepository;
    private final ClassRoomService classRoomService;

    @Transactional
    public Long createCoupon(final Member admin, final String code, final Long classRoomId,
                              final int discountRate, final int maxUsagePerMember) {
        validateAdmin(admin);

        if (couponRepository.existsByCode(code)) {
            throw new CustomException(Error.DUPLICATE_COUPON_CODE, Error.DUPLICATE_COUPON_CODE.getMessage());
        }
        if (discountRate <= 0 || discountRate > 100) {
            throw new CustomException(Error.INVALID_COUPON_DISCOUNT_RATE, Error.INVALID_COUPON_DISCOUNT_RATE.getMessage());
        }

        ClassRoomEntity classRoom = classRoomService.findById(classRoomId);

        Coupon coupon = Coupon.builder()
                .code(code)
                .classRoom(classRoom)
                .discountRate(discountRate)
                .maxUsagePerMember(maxUsagePerMember)
                .build();

        return couponRepository.save(coupon).getId();
    }

    @Transactional(readOnly = true)
    public CouponValidationResponse validate(final Member member, final String code, final Long classRoomId) {
        Coupon coupon = getUsableCoupon(member, code, classRoomId);

        ClassRoomEntity classRoom = coupon.getClassRoom();
        int basePrice = classRoom.getPriceDetail().getDiscountPrice();
        int discountedPrice = (int) Math.round(basePrice * (1 - coupon.getDiscountRate() / 100.0));

        return CouponValidationResponse.of(coupon, discountedPrice);
    }

    // 결제 승인(confirm) 성공 이후 호출 - 실제 사용 처리(사용 횟수 차감)
    @Transactional
    public void redeem(final Member member, final String code, final Long classRoomId, final String orderId) {
        Coupon coupon = getUsableCoupon(member, code, classRoomId);

        if (couponUsageRepository.existsByCouponAndOrderId(coupon, orderId)) {
            // 이미 같은 주문건으로 사용 처리된 경우 (중복 호출 방지)
            return;
        }

        CouponUsage usage = CouponUsage.builder()
                .coupon(coupon)
                .member(member)
                .orderId(orderId)
                .build();
        couponUsageRepository.save(usage);
    }

    private Coupon getUsableCoupon(final Member member, final String code, final Long classRoomId) {
        Coupon coupon = couponRepository.findByCode(code).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_COUPON, Error.NOT_FOUND_COUPON.getMessage())
        );

        if (!coupon.getClassRoom().getId().equals(classRoomId)) {
            throw new CustomException(Error.NOT_APPLICABLE_COUPON, Error.NOT_APPLICABLE_COUPON.getMessage());
        }

        int usedCount = couponUsageRepository.countByCouponAndMember(coupon, member);
        if (usedCount >= coupon.getMaxUsagePerMember()) {
            throw new CustomException(Error.EXCEED_COUPON_USAGE_LIMIT, Error.EXCEED_COUPON_USAGE_LIMIT.getMessage());
        }

        return coupon;
    }

    private void validateAdmin(final Member member) {
        if (!member.getRole().equals(Role.ROLE_ADMIN)) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }
    }
}
