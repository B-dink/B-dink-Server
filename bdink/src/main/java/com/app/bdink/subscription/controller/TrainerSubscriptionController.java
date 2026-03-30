package com.app.bdink.subscription.controller;

import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.subscription.controller.dto.request.TrainerMembershipCancelRequest;
import com.app.bdink.subscription.controller.dto.request.TrainerMembershipCreateRequest;
import com.app.bdink.subscription.controller.dto.request.TrainerMembershipRenewRequest;
import com.app.bdink.subscription.controller.dto.response.TrainerMembershipPlanResponse;
import com.app.bdink.subscription.controller.dto.response.TrainerMembershipResponse;
import com.app.bdink.subscription.service.TrainerSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
@Tag(name = "구독 API", description = "트레이너 정기결제 구독 관련 API입니다.")
public class TrainerSubscriptionController {

    private final TrainerSubscriptionService trainerSubscriptionService;
    private final MemberUtilService memberUtilService;
    private final MemberService memberService;

    @GetMapping("/plans")
    @Operation(method = "GET", description = "활성화된 구독 플랜 목록을 조회합니다.")
    public RspTemplate<?> getSubscriptionPlans() {
        List<TrainerMembershipPlanResponse> responses = trainerSubscriptionService.getActivePlans().stream()
                .map(TrainerMembershipPlanResponse::from)
                .toList();
        return RspTemplate.success(Success.GET_TRAINER_MEMBERSHIP_PLAN_SUCCESS, responses);
    }

    @PostMapping
    @Operation(method = "POST", description = "클라이언트 결제 완료 후 트레이너 구독을 생성합니다.")
    public RspTemplate<?> createSubscription(Principal principal,
                                             @RequestBody TrainerMembershipCreateRequest request) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));

        String subscriptionId = trainerSubscriptionService.createSubscriptionForMember(
                member,
                request.subscriptionPlanId(),
                request.paymentDate(),
                request.autoRenew()
        ).getId().toString();

        return RspTemplate.success(Success.CREATE_TRAINER_MEMBERSHIP_SUCCESS, CreateIdDto.from(subscriptionId));
    }

    @GetMapping("/{id}")
    @Operation(method = "GET", description = "구독 정보를 조회합니다.")
    public RspTemplate<?> getSubscription(@PathVariable Long id) {
        return RspTemplate.success(
                Success.GET_TRAINER_MEMBERSHIP_SUCCESS,
                TrainerMembershipResponse.from(trainerSubscriptionService.findById(id))
        );
    }

    @GetMapping("/trainer/{trainerId}")
    @Operation(method = "GET", description = "트레이너의 활성 구독 정보를 조회합니다.")
    public RspTemplate<?> getActiveSubscription(@PathVariable Long trainerId) {
        return RspTemplate.success(
                Success.GET_TRAINER_MEMBERSHIP_SUCCESS,
                TrainerMembershipResponse.from(trainerSubscriptionService.getActiveSubscription(trainerId))
        );
    }

    @PostMapping("/{id}/renew")
    @Operation(method = "POST", description = "클라이언트 결제 완료 후 기존 구독의 결제일을 갱신합니다.")
    public RspTemplate<?> renewSubscription(@PathVariable Long id,
                                            @RequestBody TrainerMembershipRenewRequest request) {
        return RspTemplate.success(
                Success.UPDATE_TRAINER_MEMBERSHIP_SUCCESS,
                TrainerMembershipResponse.from(trainerSubscriptionService.renewSubscription(id, request.paymentDate()))
        );
    }

    @PostMapping("/{id}/cancel")
    @Operation(method = "POST", description = "자동결제를 해지합니다.")
    public RspTemplate<?> cancelSubscription(@PathVariable Long id,
                                             @RequestBody TrainerMembershipCancelRequest request) {
        return RspTemplate.success(
                Success.UPDATE_TRAINER_MEMBERSHIP_SUCCESS,
                TrainerMembershipResponse.from(trainerSubscriptionService.cancelSubscription(id, request.canceledDate()))
        );
    }
}
