package com.app.bdink.sugang.entity;

import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "completed")
    private boolean completed;

    public Sugang(Lecture lecture, Member member) {
        this.lecture = lecture;
        this.member = member;
        this.completed = false;  // 기본값: 수강하지 않은 상태
    }

    public boolean isCompleted() {
        return completed;
    }
}
