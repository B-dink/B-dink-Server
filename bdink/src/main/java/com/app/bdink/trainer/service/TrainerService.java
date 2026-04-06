package com.app.bdink.trainer.service;

import com.app.bdink.center.entity.Center;
import com.app.bdink.center.service.CenterService;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import com.app.bdink.notification.entity.NotificationLinkType;
import com.app.bdink.notification.entity.NotificationType;
import com.app.bdink.notification.service.NotificationFactory;
import com.app.bdink.notification.service.NotificationService;
import com.app.bdink.trainer.controller.dto.request.TrainerCreateRequest;
import com.app.bdink.trainer.controller.dto.request.TrainerProfileCompleteRequest;
import com.app.bdink.trainer.controller.dto.request.TrainerQrVerifyRequest;
import com.app.bdink.trainer.controller.dto.request.TrainerUpdateRequest;
import com.app.bdink.trainer.controller.dto.response.TrainerResponse;
import com.app.bdink.trainer.entity.Trainer;
import com.app.bdink.trainer.entity.TrainerStatus;
import com.app.bdink.trainer.repository.TrainerRepository;
import com.app.bdink.trainermember.controller.dto.request.TrainerMemberCreateRequest;
import com.app.bdink.trainermember.service.TrainerMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 트레이너 도메인 비즈니스 로직을 담당한다.
 * 생성 시 기존 비활성 트레이너가 있으면 재활성화한다.
 */
@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final CenterService centerService;
    private final TrainerMemberService trainerMemberService;
    private final NotificationService notificationService;
    private final NotificationFactory notificationFactory;

    /**
     * 트레이너를 생성한다.
     * 이미 비활성 트레이너가 존재하면 해당 엔티티를 갱신 후 활성화한다.
     */
    @Transactional
    public String createTrainer(Member member, TrainerCreateRequest request, String profileImageKey) {
        Center center = centerService.findById(request.centerId());

        Trainer existingTrainer = trainerRepository.findByMemberId(member.getId()).orElse(null);
        if (existingTrainer != null) {
            if (existingTrainer.getStatus() == TrainerStatus.ACTIVE) {
                throw new CustomException(Error.EXIST_TRAINER, Error.EXIST_TRAINER.getMessage());
            }
            existingTrainer.updateCenter(center);
            existingTrainer.update(member.getName(), request.career(), request.intro(), profileImageKey);
            existingTrainer.activate();
            return existingTrainer.getId().toString();
        }

        Trainer trainer = Trainer.builder()
                .center(center)
                .member(member)
                .name(member.getName())
                .career(request.career())
                .intro(request.intro())
                .profileImage(profileImageKey)
                .build();

        return trainerRepository.save(trainer).getId().toString();
    }

    /**
     * 활성 상태 트레이너만 조회한다.
     */
    @Transactional(readOnly = true)
    public Trainer getActiveTrainer(Long id) {
        return trainerRepository.findByIdAndStatus(id, TrainerStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_TRAINER, Error.NOT_FOUND_TRAINER.getMessage()));
    }

    /**
     * 멤버 기준 활성 트레이너 여부를 확인한다.
     */
    @Transactional(readOnly = true)
    public boolean isActiveTrainer(Long memberId) {
        return trainerRepository.existsByMemberIdAndStatus(memberId, TrainerStatus.ACTIVE);
    }

    /**
     * 멤버 기준 활성 트레이너만 조회한다.
     */
    @Transactional(readOnly = true)
    public Trainer getActiveTrainerByMemberId(Long memberId) {
        return trainerRepository.findByMemberIdAndStatus(memberId, TrainerStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_TRAINER, Error.NOT_FOUND_TRAINER.getMessage()));
    }

    /**
     * 결제 완료 후 트레이너 구독 생성을 위해 최소 정보의 트레이너를 생성하거나 기존 활성 트레이너를 반환한다.
     */
    @Transactional
    public Trainer getOrCreatePaidTrainer(Member member) {
        Trainer existingTrainer = trainerRepository.findByMemberId(member.getId()).orElse(null);

        if (existingTrainer != null) {
            if (existingTrainer.getStatus() == TrainerStatus.INACTIVE) {
                existingTrainer.updateCenter(null);
                existingTrainer.update(member.getName(), Career.TRAINER, null, null);
                existingTrainer.activate();
            }
            return existingTrainer;
        }

        Trainer trainer = Trainer.builder()
                .center(null)
                .member(member)
                .name(member.getName())
                .career(Career.TRAINER)
                .intro(null)
                .profileImage(null)
                .build();

        return trainerRepository.save(trainer);
    }

    /**
     * 센터 기준 활성 트레이너 목록을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<Trainer> getActiveTrainersByCenter(Long centerId) {
        return trainerRepository.findAllByCenterIdAndStatus(centerId, TrainerStatus.ACTIVE);
    }

    /**
     * 여러 센터 기준 활성 트레이너 목록을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<Trainer> getActiveTrainersByCenters(List<Long> centerIds) {
        if (centerIds.isEmpty()) {
            return List.of();
        }
        return trainerRepository.findAllByCenterIdInAndStatus(centerIds, TrainerStatus.ACTIVE);
    }

    /**
     * 트레이너 프로필 정보를 수정한다.
     */
    @Transactional
    public TrainerResponse updateTrainer(Long id, TrainerUpdateRequest request, String profileImageKey) {
        Trainer trainer = getActiveTrainer(id);
        trainer.update(request.name(), request.career(), request.intro(), profileImageKey);
        return TrainerResponse.from(trainer);
    }

    /**
     * 결제 후 자동 생성된 트레이너의 프로필을 보완한다.
     */
    @Transactional
    public TrainerResponse completeTrainerProfile(Long memberId, TrainerProfileCompleteRequest request, String profileImageKey) {
        Trainer trainer = getActiveTrainerByMemberId(memberId);
        Center center = centerService.findById(request.centerId());

        trainer.updateCenter(center);
        trainer.update(request.trainerName(), Career.TRAINER, request.intro(), profileImageKey);

        return TrainerResponse.from(trainer);
    }

    /**
     * 트레이너 QR 토큰을 업데이트한다. null 허용.
     */
    @Transactional
    public TrainerResponse updateQrToken(Long trainerId, String qrToken) {
        Trainer trainer = getActiveTrainer(trainerId);
        trainer.updateQrToken(qrToken);
        return TrainerResponse.from(trainer);
    }

    /**
     * QR 토큰으로 트레이너를 찾아 소속을 생성한다.
     * 이미 다른 트레이너 소속이면 createTrainerMember에서 거절된다.
     */
    @Transactional
    public String verifyTrainerQrToken(Member member, TrainerQrVerifyRequest request) {
        String qrToken = request.qrToken();
        if (qrToken == null || qrToken.isBlank()) {
            throw new CustomException(Error.INVALID_QR_TOKEN_EXCEPTION, Error.INVALID_QR_TOKEN_EXCEPTION.getMessage());
        }

        List<Trainer> trainers = trainerRepository.findAllByQrTokenAndStatus(qrToken, TrainerStatus.ACTIVE);
        if (trainers.isEmpty()) {
            throw new CustomException(Error.INVALID_QR_TOKEN_EXCEPTION, Error.INVALID_QR_TOKEN_EXCEPTION.getMessage());
        }
        if (trainers.size() > 1) {
            throw new CustomException(Error.INVALID_QR_TOKEN_EXCEPTION, Error.INVALID_QR_TOKEN_EXCEPTION.getMessage());
        }

        Trainer trainer = trainers.get(0);
        if (trainer.getMember().getId().equals(member.getId())) {
            throw new CustomException(Error.INVALID_SELF_TRAINER_QR, Error.INVALID_SELF_TRAINER_QR.getMessage());
        }

        String trainerMemberId = trainerMemberService.createTrainerMember(
                new TrainerMemberCreateRequest(trainer.getId(), member.getId())
        );
        notificationService.create(notificationFactory.create(
                member.getId(),
                NotificationType.TRAINER_REGISTERED,
                "트레이너 등록 완료",
                "트레이너 등록이 완료되었습니다.",
                NotificationLinkType.TRAINER_PROFILE,
                trainer.getId()
        ));
        notificationService.create(notificationFactory.create(
                trainer.getMember().getId(),
                NotificationType.TRAINER_REGISTERED,
                "새 회원 등록",
                "새 회원이 등록되었습니다.",
                NotificationLinkType.TRAINER_MEMBER_LIST,
                trainer.getId()
        ));

        return trainerMemberId;
    }

    /**
     * 트레이너를 soft delete 처리한다.
     */
    @Transactional
    public void deleteTrainer(Long id) {
        Trainer trainer = getActiveTrainer(id);
        trainer.deactivate();
    }
}
