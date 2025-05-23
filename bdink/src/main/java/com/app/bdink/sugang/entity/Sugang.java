package com.app.bdink.sugang.entity;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.member.entity.Member;
import com.app.bdink.sugang.controller.dto.SugangStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Sugang extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClassRoomEntity classRoomEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    private SugangStatus sugangStatus;

    @Column(nullable = false)
    private double progressPercent; // 수강한 클래스룸 전체 진행률

    @Column(nullable = false)
    private LocalDate expiredDate;

    // 생성 시 기준 3개월 유효기간 (createdAt + 3개월과 동의어로 취급)
    @PrePersist
    public void prePersist() {
        if (this.expiredDate == null) {
            this.expiredDate = LocalDate.now().plusMonths(3);
        }
    }

    @Builder
    public Sugang(ClassRoomEntity classRoomEntity, Member member, SugangStatus sugangStatus) {
        this.classRoomEntity = classRoomEntity;
        this.member = member;
        this.sugangStatus = sugangStatus;
        this.progressPercent = 0;
        this.expiredDate = LocalDate.now().plusMonths(3);
    }

    public void updateProgressPercent(double progressPercent) {
        this.progressPercent = progressPercent;
    }

}
