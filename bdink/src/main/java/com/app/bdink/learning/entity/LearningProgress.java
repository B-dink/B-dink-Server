package com.app.bdink.learning.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_learning_progress_member_lecture", columnNames = {"member_id", "lecture_id"})
        }
)
public class LearningProgress extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @Column(nullable = false)
    private int lastPositionSec;

    @Column(nullable = false)
    private int maxPositionSec;

    @Column(nullable = false)
    private int durationSec;

    @Column(nullable = false)
    private double progressPercent;

    @Column(nullable = false)
    private boolean completed;

    private LocalDateTime firstWatchedAt;

    private LocalDateTime lastWatchedAt;

    private LocalDateTime completedAt;

    @Builder
    public LearningProgress(Member member, Lecture lecture, int lastPositionSec, int maxPositionSec,
                            int durationSec, double progressPercent, boolean completed,
                            LocalDateTime firstWatchedAt, LocalDateTime lastWatchedAt, LocalDateTime completedAt) {
        this.member = member;
        this.lecture = lecture;
        this.lastPositionSec = lastPositionSec;
        this.maxPositionSec = maxPositionSec;
        this.durationSec = durationSec;
        this.progressPercent = progressPercent;
        this.completed = completed;
        this.firstWatchedAt = firstWatchedAt;
        this.lastWatchedAt = lastWatchedAt;
        this.completedAt = completedAt;
    }

    public static LearningProgress create(Member member, Lecture lecture, int lastPositionSec, int maxPositionSec, int durationSec,
                                          double progressPercent, boolean completed) {
        LocalDateTime now = LocalDateTime.now();
        // 최초 학습 시작 시점의 스냅샷을 생성한다.
        return LearningProgress.builder()
                .member(member)
                .lecture(lecture)
                .lastPositionSec(lastPositionSec)
                .maxPositionSec(maxPositionSec)
                .durationSec(durationSec)
                .progressPercent(progressPercent)
                .completed(completed)
                .firstWatchedAt(now)
                .lastWatchedAt(now)
                .completedAt(completed ? now : null)
                .build();
    }

    public void updateProgress(int lastPositionSec, int maxPositionSec, int durationSec,
                               double progressPercent, boolean completed) {
        // 마지막 재생 위치는 사용자의 실제 종료 지점을 보여주기 때문에 항상 최신값으로 반영한다.
        this.lastPositionSec = lastPositionSec;
        // 인정된 최대 진도율은 뒤로 이동(seek back)하더라도 감소하지 않도록 유지한다.
        this.maxPositionSec = Math.max(this.maxPositionSec, maxPositionSec);
        this.durationSec = durationSec;
        // 퍼센트 역시 감소하지 않도록 가장 큰 값만 유지한다.
        this.progressPercent = Math.max(this.progressPercent, progressPercent);
        this.lastWatchedAt = LocalDateTime.now();

        // 한번 완강 처리된 강의는 다시 미완료 상태로 되돌리지 않는다.
        if (!this.completed && completed) {
            this.completed = true;
            this.completedAt = LocalDateTime.now();
        }
    }
}
