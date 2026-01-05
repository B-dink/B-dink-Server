package com.app.bdink.trainer.controller;

import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.external.aws.service.S3Service;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.trainer.controller.dto.request.TrainerCreateRequest;
import com.app.bdink.trainer.controller.dto.request.TrainerQrTokenUpdateRequest;
import com.app.bdink.trainer.controller.dto.request.TrainerQrVerifyRequest;
import com.app.bdink.trainer.controller.dto.request.TrainerUpdateRequest;
import com.app.bdink.trainer.controller.dto.response.IsTrainerResponse;
import com.app.bdink.trainer.controller.dto.response.TrainerResponse;
import com.app.bdink.trainer.entity.Trainer;
import com.app.bdink.trainer.service.TrainerService;
import com.app.bdink.trainermember.controller.dto.response.TrainerMemberWeeklyVolumeResponse;
import com.app.bdink.trainermember.service.TrainerMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

/**
 * 트레이너 CRUD API 컨트롤러.
 * 프로필 수정/삭제는 본인만 가능하고, QR 토큰 갱신은 제한을 두지 않는다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/v1/trainer"})
@Tag(name = "트레이너 API", description = "트레이너 관련 CRUD API입니다.")
public class TrainerController {

    private final TrainerService trainerService;
    private final MemberService memberService;
    private final MemberUtilService memberUtilService;
    private final TrainerMemberService trainerMemberService;
    private final S3Service s3Service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(method = "POST", description = "트레이너를 생성합니다.")
    public RspTemplate<?> createTrainer(Principal principal,
                                        @RequestPart(value = "trainerDto") TrainerCreateRequest request,
                                        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        // 토큰의 주체(memberId)로 트레이너를 생성한다.
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        String profileImageKey = null;

        if (profileImage != null && !profileImage.isEmpty()) {
            profileImageKey = s3Service.uploadImageOrMedia("image/", profileImage);
        }

        String trainerId = trainerService.createTrainer(member, request, profileImageKey);
        return RspTemplate.success(Success.CREATE_TRAINER_SUCCESS, CreateIdDto.from(trainerId));
    }

    @GetMapping("/{id}")
    @Operation(method = "GET", description = "트레이너 정보를 조회합니다.")
    public RspTemplate<?> getTrainer(@PathVariable Long id) {
        Trainer trainer = trainerService.getActiveTrainer(id);
        return RspTemplate.success(Success.GET_TRAINER_SUCCESS, TrainerResponse.from(trainer));
    }

    @GetMapping("/center/{centerId}")
    @Operation(method = "GET", description = "센터별 트레이너 목록을 조회합니다.")
    public RspTemplate<?> getTrainersByCenter(@PathVariable Long centerId) {
        List<TrainerResponse> responses = trainerService.getActiveTrainersByCenter(centerId).stream()
                .map(TrainerResponse::from)
                .toList();
        return RspTemplate.success(Success.GET_TRAINER_SUCCESS, responses);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(method = "PUT", description = "트레이너 정보를 수정합니다.")
    public RspTemplate<?> updateTrainer(Principal principal,
                                        @PathVariable Long id,
                                        @RequestPart(value = "trainerDto") TrainerUpdateRequest request,
                                        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        Long memberId = memberUtilService.getMemberId(principal);
        Trainer trainer = trainerService.getActiveTrainer(id);

        // 본인 트레이너만 수정 가능
        if (!trainer.getMember().getId().equals(memberId)) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }

        String profileImageKey = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            profileImageKey = s3Service.uploadImageOrMedia("image/", profileImage);
        }

        TrainerResponse response = trainerService.updateTrainer(id, request, profileImageKey);
        return RspTemplate.success(Success.UPDATE_TRAINER_SUCCESS, response);
    }

    @PutMapping("/{id}/qr-token")
    @Operation(method = "PUT", description = "트레이너 QR 토큰을 수정합니다.")
    public RspTemplate<?> updateTrainerQrToken(@PathVariable Long id,
                                               @RequestBody TrainerQrTokenUpdateRequest request) {
        TrainerResponse response = trainerService.updateQrToken(id, request.qrToken());
        return RspTemplate.success(Success.UPDATE_TRAINER_SUCCESS, response);
    }

    @PostMapping("/qrToken")
    @Operation(method = "POST", description = "QR 토큰으로 트레이너 소속을 생성합니다.")
    public RspTemplate<?> verifyTrainerQrToken(Principal principal,
                                               @RequestBody TrainerQrVerifyRequest request) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        String trainerMemberId = trainerService.verifyTrainerQrToken(member, request);
        return RspTemplate.success(Success.CREATE_TRAINER_MEMBER_SUCCESS, CreateIdDto.from(trainerMemberId));
    }

    @GetMapping("/is-trainer")
    @Operation(method = "GET", description = "현재 로그인한 사용자가 트레이너인지 확인합니다.")
    public RspTemplate<?> isTrainer(Principal principal) {
        Long memberId = memberUtilService.getMemberId(principal);
        boolean isTrainer = trainerService.isActiveTrainer(memberId);
        return RspTemplate.success(Success.GET_TRAINER_SUCCESS, IsTrainerResponse.from(isTrainer));
    }

    @GetMapping("/trainermember")
    @Operation(method = "GET", description = "트레이너 관리 회원의 주간 볼륨 변화를 조회합니다.")
    public RspTemplate<?> getTrainerMembersWeeklyVolume(Principal principal) {
        Long memberId = memberUtilService.getMemberId(principal);
        Trainer trainer = trainerService.getActiveTrainerByMemberId(memberId);

        List<TrainerMemberWeeklyVolumeResponse> responses = trainerMemberService
                .getWeeklyVolumeDeltaByTrainer(trainer.getId(), java.time.LocalDate.now());
        return RspTemplate.success(Success.GET_TRAINER_MEMBER_SUCCESS, responses);
    }

    @DeleteMapping("/{id}")
    @Operation(method = "DELETE", description = "트레이너 정보를 삭제합니다. soft delete로 진행됩니다.")
    public RspTemplate<?> deleteTrainer(Principal principal, @PathVariable Long id) {
        Long memberId = memberUtilService.getMemberId(principal);
        Trainer trainer = trainerService.getActiveTrainer(id);

        // 본인 트레이너만 삭제 가능
        if (!trainer.getMember().getId().equals(memberId)) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }

        trainerService.deleteTrainer(id);
        return RspTemplate.success(Success.DELETE_TRAINER_SUCCESS, Success.DELETE_TRAINER_SUCCESS.getMessage());
    }
}
