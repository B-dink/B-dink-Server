package com.app.bdink.trainermember.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.trainer.entity.Trainer;
import com.app.bdink.trainer.entity.TrainerStatus;
import com.app.bdink.trainer.repository.TrainerRepository;
import com.app.bdink.trainermember.controller.dto.request.TrainerMemberCreateRequest;
import com.app.bdink.trainermember.controller.dto.request.TrainerMemberUpdateRequest;
import com.app.bdink.trainermember.controller.dto.response.TrainerMemberResponse;
import com.app.bdink.trainermember.entity.TrainerMember;
import com.app.bdink.trainermember.entity.TrainerMemberStatus;
import com.app.bdink.trainermember.repository.TrainerMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 트레이너-멤버 소속 비즈니스 로직을 담당한다.
 * 생성 시 기존 비활성 소속이 있으면 재활성화한다.
 */
@Service
@RequiredArgsConstructor
public class TrainerMemberService {

    private final TrainerMemberRepository trainerMemberRepository;
    private final TrainerRepository trainerRepository;
    private final MemberService memberService;

    /**
     * 트레이너 소속 멤버를 생성한다.
     * 이미 비활성 소속이 존재하면 트레이너를 갱신하고 활성화한다.
     */
    @Transactional
    public String createTrainerMember(TrainerMemberCreateRequest request) {
        Trainer trainer = trainerRepository.findByIdAndStatus(request.trainerId(), TrainerStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_TRAINER, Error.NOT_FOUND_TRAINER.getMessage()));

        Member member = memberService.findById(request.memberId());

        TrainerMember existingMember = trainerMemberRepository.findByMemberId(request.memberId()).orElse(null);
        if (existingMember != null) {
            if (existingMember.getStatus() == TrainerMemberStatus.ACTIVE) {
                throw new CustomException(Error.EXIST_TRAINER_MEMBER, Error.EXIST_TRAINER_MEMBER.getMessage());
            }
            existingMember.updateTrainer(trainer);
            existingMember.activate();
            return existingMember.getId().toString();
        }

        TrainerMember trainerMember = TrainerMember.builder()
                .trainer(trainer)
                .member(member)
                .build();

        return trainerMemberRepository.save(trainerMember).getId().toString();
    }

    /**
     * 활성 상태 소속만 조회한다.
     */
    @Transactional(readOnly = true)
    public TrainerMember getActiveTrainerMember(Long id) {
        return trainerMemberRepository.findByIdAndStatus(id, TrainerMemberStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_TRAINER_MEMBER, Error.NOT_FOUND_TRAINER_MEMBER.getMessage()));
    }

    /**
     * 트레이너 기준 활성 소속 목록을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<TrainerMember> getActiveTrainerMembersByTrainer(Long trainerId) {
        return trainerMemberRepository.findAllByTrainerIdAndStatus(trainerId, TrainerMemberStatus.ACTIVE);
    }

    /**
     * 소속 트레이너를 변경한다. (멤버 이동)
     */
    @Transactional
    public TrainerMemberResponse updateTrainerMember(Long id, TrainerMemberUpdateRequest request) {
        TrainerMember trainerMember = getActiveTrainerMember(id);

        Trainer trainer = trainerRepository.findByIdAndStatus(request.trainerId(), TrainerStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_TRAINER, Error.NOT_FOUND_TRAINER.getMessage()));

        trainerMember.updateTrainer(trainer);
        return TrainerMemberResponse.from(trainerMember);
    }

    /**
     * 트레이너 소속을 soft delete 처리한다.
     */
    @Transactional
    public void deleteTrainerMember(Long id) {
        TrainerMember trainerMember = getActiveTrainerMember(id);
        trainerMember.deactivate();
    }
}
