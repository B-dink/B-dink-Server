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
import com.app.bdink.trainermember.controller.dto.response.TrainerMemberWeeklyVolumeResponse;
import com.app.bdink.trainermember.entity.TrainerMember;
import com.app.bdink.trainermember.entity.TrainerMemberStatus;
import com.app.bdink.trainermember.repository.TrainerMemberRepository;
import com.app.bdink.workout.repository.WorkoutSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final WorkoutSetRepository workoutSetRepository;

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

    /**
     * 트레이너가 관리하는 회원의 전주 대비 이번주 볼륨 변화량을 조회한다.
     */
    @Transactional(readOnly = true)
    public List<TrainerMemberWeeklyVolumeResponse> getWeeklyVolumeDeltaByTrainer(Long trainerId, LocalDate baseDate) {
        List<TrainerMember> trainerMembers = trainerMemberRepository
                .findAllByTrainerIdAndStatusWithMember(trainerId, TrainerMemberStatus.ACTIVE);

        if (trainerMembers.isEmpty()) {
            return List.of();
        }

        List<Long> memberIds = trainerMembers.stream()
                .map(trainerMember -> trainerMember.getMember().getId())
                .toList();

        LocalDate thisWeekStart = baseDate.with(DayOfWeek.MONDAY);
        LocalDate thisWeekEnd = thisWeekStart.plusDays(6);
        LocalDate lastWeekStart = thisWeekStart.minusWeeks(1);
        LocalDate lastWeekEnd = thisWeekStart.minusDays(1);

        LocalDateTime thisWeekFrom = thisWeekStart.atStartOfDay();
        LocalDateTime thisWeekTo = thisWeekEnd.plusDays(1).atStartOfDay();
        LocalDateTime lastWeekFrom = lastWeekStart.atStartOfDay();
        LocalDateTime lastWeekTo = lastWeekEnd.plusDays(1).atStartOfDay();

        Map<Long, Long> thisWeekVolumeMap = workoutSetRepository
                .findWeeklyVolumeByMemberIds(memberIds, thisWeekFrom, thisWeekTo)
                .stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),
                        row -> ((Number) row[1]).longValue()
                ));

        Map<Long, Long> lastWeekVolumeMap = workoutSetRepository
                .findWeeklyVolumeByMemberIds(memberIds, lastWeekFrom, lastWeekTo)
                .stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),
                        row -> ((Number) row[1]).longValue()
                ));

        return trainerMembers.stream()
                .map(trainerMember -> {
                    Long memberId = trainerMember.getMember().getId();
                    long thisWeekVolume = thisWeekVolumeMap.getOrDefault(memberId, 0L);
                    long lastWeekVolume = lastWeekVolumeMap.getOrDefault(memberId, 0L);
                    long delta = thisWeekVolume - lastWeekVolume;
                    boolean increased = delta > 0;

                    return new TrainerMemberWeeklyVolumeResponse(
                            memberId,
                            trainerMember.getMember().getName(),
                            delta,
                            increased
                    );
                })
                .sorted(Comparator.comparing(TrainerMemberWeeklyVolumeResponse::memberName,
                        Comparator.nullsLast(String::compareTo)))
                .toList();
    }
}
