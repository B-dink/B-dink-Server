package com.app.bdink.workout.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PerformedExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private WorkOutSession workOutSession;

    @ManyToOne(fetch = FetchType.LAZY)
    private Exercise exercise;

    //TODO: 현재 선택한 운동에 대해 개별 메모, 필요하면 사용할 수 도?
//    @Column(name = "memo")
//    private String memo;

    @Builder
    public PerformedExercise(WorkOutSession workOutSession, Exercise exercise) {
    }
}
