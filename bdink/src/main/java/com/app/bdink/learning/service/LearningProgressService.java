package com.app.bdink.learning.service;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.learning.controller.dto.request.LearningProgressRequest;
import com.app.bdink.learning.controller.dto.response.LearningProgressResponse;
import com.app.bdink.learning.entity.LearningEventType;
import com.app.bdink.learning.entity.LearningProgress;
import com.app.bdink.learning.entity.LearningProgressEvent;
import com.app.bdink.learning.repository.LearningProgressEventRepository;
import com.app.bdink.learning.repository.LearningProgressRepository;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.lecture.repository.LectureRepository;
import com.app.bdink.member.entity.Member;
import com.app.bdink.sugang.repository.SugangRepository;
import com.app.bdink.sugang.service.SugangService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LearningProgressService {

    private static final double COMPLETE_PERCENT = 90.0;

    private final LearningProgressRepository learningProgressRepository;
    private final LearningProgressEventRepository learningProgressEventRepository;
    private final LectureRepository lectureRepository;
    private final SugangRepository sugangRepository;
    private final SugangService sugangService;

    @Transactional(readOnly = true)
    public LearningProgressResponse getLearningProgress(Member member, Long lectureId) {
        Lecture lecture = findLecture(lectureId);

        // 저장된 진행률이 없으면 아직 학습 이력이 없는 것으로 보고 0% 기본값을 반환한다.
        return learningProgressRepository.findByMemberAndLecture(member, lecture)
                .map(progress -> new LearningProgressResponse(
                        lecture.getId(),
                        progress.getLastPositionSec(),
                        progress.getMaxPositionSec(),
                        progress.getDurationSec(),
                        progress.getProgressPercent(),
                        progress.isCompleted(),
                        progress.getLastWatchedAt()
                ))
                .orElse(new LearningProgressResponse(lecture.getId(), 0, 0, 0, 0.0, false, null));
    }

    @Transactional
    public LearningProgressResponse saveLearningProgress(Member member, Long lectureId, LearningProgressRequest request) {
        Lecture lecture = findLecture(lectureId);
        ClassRoomEntity classRoomEntity = lecture.getClassRoom();

        validateRequest(request);
        // 수강하지 않은 강의의 진행률은 저장할 수 없도록 막는다.
        validateSugang(member, classRoomEntity);

        int durationSec = request.durationSec();
        int normalizedPosition = normalizePosition(request.positionSec(), durationSec);
        // seek/play 이벤트는 로그만 남기고, 실제 진도 반영은 heartbeat/pause/ended에만 허용한다.
        int initialMaxPosition = shouldAdvance(request.eventType()) ? normalizedPosition : 0;

        saveEvent(member, lecture, request, normalizedPosition);

        // 최초 진입이면 새 학습 상태를 만들고, 이미 있으면 기존 상태를 갱신한다.
        LearningProgress progress = learningProgressRepository.findByMemberAndLecture(member, lecture)
                .orElseGet(() -> LearningProgress.create(
                        member,
                        lecture,
                        normalizedPosition,
                        initialMaxPosition,
                        durationSec,
                        calculatePercent(initialMaxPosition, durationSec),
                        isCompleted(initialMaxPosition, durationSec)
                ));

        int maxPositionSec = progress.getMaxPositionSec();
        if (shouldAdvance(request.eventType())) {
            // 인정 가능한 이벤트에서만 최대 시청 위치를 앞으로 전진시킨다.
            maxPositionSec = Math.max(maxPositionSec, normalizedPosition);
        }

        double progressPercent = calculatePercent(maxPositionSec, durationSec);
        // 완강 기준은 90%이며, 한번 완료되면 이후 요청에서도 완료 상태를 유지한다.
        boolean completed = progress.isCompleted() || progressPercent >= COMPLETE_PERCENT;

        progress.updateProgress(normalizedPosition, maxPositionSec, durationSec, progressPercent, completed);
        learningProgressRepository.save(progress);

        // 강의 단위 상태가 바뀌면 클래스룸 단위 진행률 캐시도 즉시 갱신한다.
        sugangService.updateSugangProgressByClassRoom(member, classRoomEntity);

        return new LearningProgressResponse(
                lecture.getId(),
                progress.getLastPositionSec(),
                progress.getMaxPositionSec(),
                progress.getDurationSec(),
                progress.getProgressPercent(),
                progress.isCompleted(),
                progress.getLastWatchedAt()
        );
    }

    private Lecture findLecture(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_LECTURE, Error.NOT_FOUND_LECTURE.getMessage()));
    }

    private void validateRequest(LearningProgressRequest request) {
        if (request.durationSec() <= 0 || request.eventType() == null) {
            throw new CustomException(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage());
        }
    }

    private void validateSugang(Member member, ClassRoomEntity classRoomEntity) {
        sugangRepository.findByMemberAndClassRoomEntity(member, classRoomEntity)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_SUGANG, Error.NOT_FOUND_SUGANG.getMessage()));
    }

    private int normalizePosition(int positionSec, int durationSec) {
        // 음수 재생 위치나 총 길이를 넘긴 값이 들어와도 서버에서 안전하게 보정한다.
        if (positionSec < 0) {
            return 0;
        }
        return Math.min(positionSec, durationSec);
    }

    private void saveEvent(Member member, Lecture lecture, LearningProgressRequest request, int positionSec) {
        // 최종 상태와 별개로 원본 이벤트를 저장해 추후 분석과 장애 대응에 활용한다.
        LearningProgressEvent event = LearningProgressEvent.builder()
                .member(member)
                .lecture(lecture)
                .eventType(request.eventType())
                .positionSec(positionSec)
                .durationSec(request.durationSec())
                .sessionId(request.sessionId())
                .clientOccurredAt(request.clientOccurredAt())
                .build();
        learningProgressEventRepository.save(event);
    }

    private boolean shouldAdvance(LearningEventType eventType) {
        // 실제 시청이 발생했다고 볼 수 있는 이벤트만 진도 증가 대상으로 인정한다.
        return eventType == LearningEventType.HEARTBEAT
                || eventType == LearningEventType.PAUSE
                || eventType == LearningEventType.ENDED;
    }

    private double calculatePercent(int positionSec, int durationSec) {
        // 잘못된 요청이 와도 100%를 넘지 않도록 상한을 둔다.
        return Math.min(100.0, ((double) positionSec / durationSec) * 100.0);
    }

    private boolean isCompleted(int positionSec, int durationSec) {
        return calculatePercent(positionSec, durationSec) >= COMPLETE_PERCENT;
    }
}
