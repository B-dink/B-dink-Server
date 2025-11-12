package com.app.bdink.workout.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkOutSession extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "memo")
    private String memo;

    @Builder
    public WorkOutSession(Member member, String memo) {
        this.member = member;
        this.memo = memo;
    }
}
