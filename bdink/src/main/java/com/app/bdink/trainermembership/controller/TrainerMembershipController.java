package com.app.bdink.trainermembership.controller;

import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.trainermembership.controller.dto.request.TrainerMembershipCreateRequest;
import com.app.bdink.trainermembership.controller.dto.response.TrainerMembershipQrInfoResponse;
import com.app.bdink.trainermembership.controller.dto.response.TrainerMembershipPlanResponse;
import com.app.bdink.trainermembership.controller.dto.response.TrainerMembershipResponse;
import com.app.bdink.trainermembership.service.TrainerMembershipService;
import com.app.bdink.trainer.entity.Trainer;
import com.app.bdink.trainer.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trainer-memberships")
@Tag(name = "트레이너 멤버십 API", description = "트레이너 멤버십 관련 API입니다.")
public class TrainerMembershipController {

    private final TrainerMembershipService trainerMembershipService;
    private final TrainerService trainerService;
    private final MemberUtilService memberUtilService;
    private final MemberService memberService;

    @GetMapping("/plans")
    @Operation(method = "GET", description = "활성화된 트레이너 멤버십 플랜 목록을 조회합니다.")
    public RspTemplate<?> getMembershipPlans() {
        List<TrainerMembershipPlanResponse> responses = trainerMembershipService.getActivePlans().stream()
                .map(TrainerMembershipPlanResponse::from)
                .toList();
        return RspTemplate.success(Success.GET_TRAINER_MEMBERSHIP_PLAN_SUCCESS, responses);
    }

    @PostMapping
    @Operation(method = "POST", description = "클라이언트 결제 완료 후 트레이너 멤버십을 생성합니다.")
    public RspTemplate<?> createMembership(Principal principal,
                                           @RequestBody TrainerMembershipCreateRequest request) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));

        String membershipId = trainerMembershipService.createMembershipForMember(
                member,
                request.trainerMembershipPlanId(),
                request.paymentDate()
        ).getId().toString();

        return RspTemplate.success(Success.CREATE_TRAINER_MEMBERSHIP_SUCCESS, CreateIdDto.from(membershipId));
    }

    @GetMapping("/{id}")
    @Operation(method = "GET", description = "멤버십 정보를 조회합니다.")
    public RspTemplate<?> getMembership(@PathVariable Long id) {
        return RspTemplate.success(
                Success.GET_TRAINER_MEMBERSHIP_SUCCESS,
                TrainerMembershipResponse.from(trainerMembershipService.findById(id))
        );
    }

    @GetMapping("/trainer/{trainerId}")
    @Operation(method = "GET", description = "트레이너의 활성 멤버십 정보를 조회합니다.")
    public RspTemplate<?> getActiveMembership(@PathVariable Long trainerId) {
        return RspTemplate.success(
                Success.GET_TRAINER_MEMBERSHIP_SUCCESS,
                TrainerMembershipResponse.from(trainerMembershipService.getActiveMembership(trainerId))
        );
    }

    @GetMapping("/me/qr-info")
    @Operation(method = "GET", description = "현재 로그인한 트레이너의 QR 이미지 주소와 멤버십 만료일을 조회합니다.")
    public RspTemplate<?> getMyMembershipQrInfo(Principal principal) {
        Long memberId = memberUtilService.getMemberId(principal);
        Trainer trainer = trainerService.getActiveTrainerByMemberId(memberId);

        return RspTemplate.success(
                Success.GET_TRAINER_MEMBERSHIP_SUCCESS,
                TrainerMembershipQrInfoResponse.from(trainer, trainerMembershipService.getMyActiveMembership(memberId))
        );
    }
}
