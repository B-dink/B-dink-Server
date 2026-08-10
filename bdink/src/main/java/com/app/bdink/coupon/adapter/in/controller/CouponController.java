package com.app.bdink.coupon.adapter.in.controller;

import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.coupon.adapter.in.controller.dto.request.CreateCouponRequest;
import com.app.bdink.coupon.adapter.in.controller.dto.response.CouponValidationResponse;
import com.app.bdink.coupon.service.CouponService;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.member.util.MemberUtilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/coupons")
@Tag(name = "쿠폰 API", description = "할인 쿠폰 생성/검증/사용과 관련된 API들입니다.")
public class CouponController {

    private final CouponService couponService;
    private final MemberService memberService;
    private final MemberUtilService memberUtilService;

    @PostMapping
    @Operation(method = "POST", description = "쿠폰을 생성합니다. (관리자 전용)")
    public RspTemplate<CreateIdDto> createCoupon(Principal principal, @RequestBody CreateCouponRequest request) {
        Member admin = memberService.findById(memberUtilService.getMemberId(principal));
        Long couponId = couponService.createCoupon(
                admin, request.code(), request.classRoomId(), request.discountRate(), request.maxUsagePerMember());
        return RspTemplate.success(Success.CREATE_COUPON_SUCCESS, CreateIdDto.from(String.valueOf(couponId)));
    }

    @GetMapping("/validate")
    @Operation(method = "GET", description = "쿠폰 코드가 해당 강의에 사용 가능한지 검증하고, 적용된 할인가를 반환합니다.")
    public RspTemplate<CouponValidationResponse> validateCoupon(
            Principal principal, @RequestParam String code, @RequestParam Long classRoomId) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        CouponValidationResponse response = couponService.validate(member, code, classRoomId);
        return RspTemplate.success(Success.VALIDATE_COUPON_SUCCESS, response);
    }

    @PostMapping("/redeem")
    @Operation(method = "POST", description = "결제 완료 후 쿠폰 사용을 확정 처리합니다.")
    public RspTemplate<?> redeemCoupon(
            Principal principal, @RequestParam String code, @RequestParam Long classRoomId,
            @RequestParam String orderId) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        couponService.redeem(member, code, classRoomId, orderId);
        return RspTemplate.success(Success.REDEEM_COUPON_SUCCESS, Success.REDEEM_COUPON_SUCCESS.getMessage());
    }
}
