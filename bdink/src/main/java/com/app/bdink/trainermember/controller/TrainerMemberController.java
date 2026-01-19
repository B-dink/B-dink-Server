package com.app.bdink.trainermember.controller;

import com.app.bdink.centerowner.service.CenterOwnerService;
import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.trainer.entity.Trainer;
import com.app.bdink.trainer.service.TrainerService;
import com.app.bdink.trainermember.controller.dto.request.TrainerMemberCreateRequest;
import com.app.bdink.trainermember.controller.dto.request.TrainerMemberUpdateRequest;
import com.app.bdink.trainermember.controller.dto.response.TrainerMemberResponse;
import com.app.bdink.trainermember.entity.TrainerMember;
import com.app.bdink.trainermember.service.TrainerMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * 트레이너-멤버 소속 CRUD API 컨트롤러.
 * 트레이너 본인 또는 해당 센터의 센터장만 접근할 수 있다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trainerMember")
@Tag(name = "트레이너 소속 멤버 API", description = "트레이너-멤버 소속 CRUD API입니다.")
public class TrainerMemberController {

    private final TrainerMemberService trainerMemberService;
    private final TrainerService trainerService;
    private final CenterOwnerService centerOwnerService;
    private final MemberUtilService memberUtilService;

    @PostMapping
    @Operation(method = "POST", description = "트레이너 소속 멤버를 생성합니다.")
    public RspTemplate<?> createTrainerMember(Principal principal,
                                              @RequestBody TrainerMemberCreateRequest request) {
        Long memberId = memberUtilService.getMemberId(principal);
        Trainer trainer = trainerService.getActiveTrainer(request.trainerId());
        // 트레이너 본인 또는 센터장만 등록 가능
        validateAccess(memberId, trainer);

        String trainerMemberId = trainerMemberService.createTrainerMember(request);
        return RspTemplate.success(Success.CREATE_TRAINER_MEMBER_SUCCESS, CreateIdDto.from(trainerMemberId));
    }

    @GetMapping("/{id}")
    @Operation(method = "GET", description = "트레이너 소속 멤버 정보를 조회합니다.")
    public RspTemplate<?> getTrainerMember(Principal principal, @PathVariable Long id) {
        Long memberId = memberUtilService.getMemberId(principal);
        TrainerMember trainerMember = trainerMemberService.getActiveTrainerMember(id);
        // 트레이너 본인 또는 센터장만 조회 가능
        validateAccess(memberId, trainerMember.getTrainer());

        return RspTemplate.success(Success.GET_TRAINER_MEMBER_SUCCESS, TrainerMemberResponse.from(trainerMember));
    }

    @GetMapping("/trainer/{trainerId}")
    @Operation(method = "GET", description = "트레이너별 소속 멤버 목록을 조회합니다.")
    public RspTemplate<?> getTrainerMembersByTrainer(Principal principal, @PathVariable Long trainerId) {
        Long memberId = memberUtilService.getMemberId(principal);
        Trainer trainer = trainerService.getActiveTrainer(trainerId);
        // 트레이너 본인 또는 센터장만 조회 가능
        validateAccess(memberId, trainer);

        List<TrainerMemberResponse> responses = trainerMemberService.getActiveTrainerMembersByTrainer(trainerId).stream()
                .map(TrainerMemberResponse::from)
                .toList();
        return RspTemplate.success(Success.GET_TRAINER_MEMBER_SUCCESS, responses);
    }

    @PutMapping("/{id}")
    @Operation(method = "PUT", description = "트레이너 소속 멤버 정보를 수정합니다.")
    public RspTemplate<?> updateTrainerMember(Principal principal,
                                              @PathVariable Long id,
                                              @RequestBody TrainerMemberUpdateRequest request) {
        Long memberId = memberUtilService.getMemberId(principal);
        TrainerMember trainerMember = trainerMemberService.getActiveTrainerMember(id);
        // 트레이너 본인 또는 센터장만 수정 가능
        validateAccess(memberId, trainerMember.getTrainer());

        TrainerMemberResponse response = trainerMemberService.updateTrainerMember(id, request);
        return RspTemplate.success(Success.UPDATE_TRAINER_MEMBER_SUCCESS, response);
    }

    @DeleteMapping("/{id}")
    @Operation(method = "DELETE", description = "트레이너 소속 멤버 정보를 삭제합니다. soft delete로 진행됩니다.")
    public RspTemplate<?> deleteTrainerMember(Principal principal, @PathVariable Long id) {
        Long memberId = memberUtilService.getMemberId(principal);
        TrainerMember trainerMember = trainerMemberService.getActiveTrainerMember(id);
        // 트레이너 본인 또는 센터장만 삭제 가능
        validateAccess(memberId, trainerMember.getTrainer());

        trainerMemberService.deleteTrainerMember(id);
        return RspTemplate.success(Success.DELETE_TRAINER_MEMBER_SUCCESS, Success.DELETE_TRAINER_MEMBER_SUCCESS.getMessage());
    }

    /**
     * 트레이너 본인 또는 센터장인지 검증한다.
     */
    private void validateAccess(Long memberId, Trainer trainer) {
        boolean isTrainer = trainer.getMember().getId().equals(memberId);
        boolean isCenterOwner = centerOwnerService.isCenterOwner(trainer.getCenter().getId(), memberId);

        if (!isTrainer && !isCenterOwner) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }
    }
}
