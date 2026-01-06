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
import com.app.bdink.trainermember.controller.dto.response.TrainerMemberWeeklyVolumeDetailResponse;
import com.app.bdink.trainermember.entity.TrainerMember;
import com.app.bdink.trainermember.entity.TrainerMemberStatus;
import com.app.bdink.trainermember.repository.TrainerMemberRepository;
import com.app.bdink.workout.controller.dto.ExercisePart;
import com.app.bdink.workout.entity.WorkOutSession;
import com.app.bdink.workout.repository.WorkoutSetRepository;
import com.app.bdink.workout.repository.WorkOutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.EnumMap;
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
    private final WorkOutSessionRepository workOutSessionRepository;

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
     * 특정 회원의 주간 일별 볼륨 데이터를 조회한다.
     * baseDate가 포함된 주(월~일)를 기준으로 계산한다.
     */
    @Transactional(readOnly = true)
    public TrainerMemberWeeklyVolumeDetailResponse getWeeklyVolumeDetailByMember(Long memberId, LocalDate baseDate) {
        TrainerMember trainerMember = trainerMemberRepository
                .findByMemberIdAndStatus(memberId, TrainerMemberStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_TRAINER_MEMBER,
                        Error.NOT_FOUND_TRAINER_MEMBER.getMessage()));

        LocalDate weekStart = baseDate.with(DayOfWeek.MONDAY);
        List<TrainerMemberWeeklyVolumeDetailResponse.DailyVolume> dailyVolumes = new ArrayList<>();

        // 월~일까지 7일간 일별 볼륨 계산
        for (int i = 0; i < 7; i++) {
            LocalDate day = weekStart.plusDays(i);
            LocalDateTime from = day.atStartOfDay();
            LocalDateTime to = day.plusDays(1).atStartOfDay();
            Long volume = workoutSetRepository.calculateVolumeForPeriod(trainerMember.getMember(), from, to);
            if (volume == null) {
                volume = 0L;
            }

            dailyVolumes.add(new TrainerMemberWeeklyVolumeDetailResponse.DailyVolume(day.toString(), volume));
        }

        // 주간 운동 부위 점수/레벨 합산 (main:2점, sub:1점, OTHERS 제외)
        Map<ExercisePart, TrainerMemberWeeklyVolumeDetailResponse.PartScore> partScores = calculatePartScores(
                trainerMember.getMember(),
                weekStart.atStartOfDay(),
                weekStart.plusDays(7).atStartOfDay()
        );

        String createdDate = trainerMember.getCreatedAt().toLocalDate().toString();
        return new TrainerMemberWeeklyVolumeDetailResponse(
                trainerMember.getMember().getId(),
                createdDate,
                dailyVolumes,
                partScores
        );
    }

    /**
     * 기간 내 운동일지를 기준으로 부위별 점수를 합산한다.
     * main 부위는 2점, sub 부위는 1점, OTHERS는 집계하지 않는다.
     */
    private Map<ExercisePart, TrainerMemberWeeklyVolumeDetailResponse.PartScore> calculatePartScores(Member member,
                                                                                                      LocalDateTime from,
                                                                                                      LocalDateTime to) {
        List<WorkOutSession> sessions = workOutSessionRepository.findByMemberAndCreatedAtBetween(member, from, to);
        Map<ExercisePart, Integer> scores = new EnumMap<>(ExercisePart.class);

        for (WorkOutSession session : sessions) {
            session.getPerformedExercises().forEach(performed -> {
                ExercisePart part = performed.getExercise().getPart();
                if (part == null || part == ExercisePart.OTHERS) {
                    return;
                }
                // main 부위는 2점, sub 부위는 1점
                int score = isMainPart(part) ? 2 : 1;
                scores.merge(part, score, Integer::sum);
            });
        }

        int totalScore = scores.values().stream().mapToInt(Integer::intValue).sum();
        Map<ExercisePart, TrainerMemberWeeklyVolumeDetailResponse.PartScore> result = new EnumMap<>(ExercisePart.class);
        scores.forEach((part, score) -> {
            int level = calcLevel(score, totalScore);
            result.put(part, new TrainerMemberWeeklyVolumeDetailResponse.PartScore(score, level));
        });

        return result;
    }

    /**
     * 주간 전체 점수 대비 비율로 레벨을 산정한다.
     * ratio = partScore / totalScore
     */
    private int calcLevel(int partScore, int totalScore) {
        if (totalScore == 0 || partScore == 0) {
            return 0;
        }
        double ratio = (double) partScore / totalScore;
        if (ratio <= 0.33) {
            return 1;
        }
        if (ratio <= 0.66) {
            return 2;
        }
        return 3;
    }

    /**
     * main 부위 여부를 판별한다.
     */
    private boolean isMainPart(ExercisePart part) {
        return part == ExercisePart.BACK
                || part == ExercisePart.CHEST
                || part == ExercisePart.SHOULDERS
                || part == ExercisePart.LEGS;
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
