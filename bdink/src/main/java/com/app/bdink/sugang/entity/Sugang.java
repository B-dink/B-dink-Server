package com.app.bdink.sugang.entity;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.member.entity.Member;
import com.app.bdink.sugang.controller.dto.SugangStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Sugang {
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

    @Builder
    public Sugang(ClassRoomEntity classRoomEntity, Member member, SugangStatus sugangStatus) {
        this.classRoomEntity = classRoomEntity;
        this.member = member;
        this.sugangStatus = sugangStatus;
        this.progressPercent = 0;
    }

    public void updateProgressPercent(double progressPercent) {
        this.progressPercent = progressPercent;
    }

}
