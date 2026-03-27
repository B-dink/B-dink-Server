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
public class LearningProgressEvent extends BaseTimeEntity {

    // 서비스 운영용 로그 역할 Entity
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LearningEventType eventType;

    @Column(nullable = false)
    private int positionSec;

    @Column(nullable = false)
    private int durationSec;

    private String sessionId;

    private LocalDateTime clientOccurredAt;

    @Builder
    public LearningProgressEvent(Member member, Lecture lecture, LearningEventType eventType, int positionSec,
                                 int durationSec, String sessionId, LocalDateTime clientOccurredAt) {
        // 진행률 계산의 원본 이벤트를 남겨 디버깅과 정책 보정에 활용한다.
        this.member = member;
        this.lecture = lecture;
        this.eventType = eventType;
        this.positionSec = positionSec;
        this.durationSec = durationSec;
        this.sessionId = sessionId;
        this.clientOccurredAt = clientOccurredAt;
    }
}
