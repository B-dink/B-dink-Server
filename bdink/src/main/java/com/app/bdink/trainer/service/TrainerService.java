package com.app.bdink.trainer.service;

import com.app.bdink.center.entity.Center;
import com.app.bdink.center.service.CenterService;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import com.app.bdink.trainer.controller.dto.request.TrainerCreateRequest;
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
     * 멤버 기준 활성 트레이너만 조회한다.
     */
    @Transactional(readOnly = true)
    public Trainer getActiveTrainerByMemberId(Long memberId) {
        return trainerRepository.findByMemberIdAndStatus(memberId, TrainerStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_TRAINER, Error.NOT_FOUND_TRAINER.getMessage()));
    }

    /**
     * 센터 기준 활성 트레이너 목록을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<Trainer> getActiveTrainersByCenter(Long centerId) {
        return trainerRepository.findAllByCenterIdAndStatus(centerId, TrainerStatus.ACTIVE);
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

        return trainerMemberService.createTrainerMember(
                new TrainerMemberCreateRequest(trainer.getId(), member.getId())
        );
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
