package com.app.bdink.workout.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkoutSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private PerformedExercise performedExercise;

    @Column(name = "setNumber")
    private int setNumber;

    @Column(name = "weight")
    private int weight;

    @Column(name = "reps")
    private int reps;

    @Column(name = "restTime")
    private int restTime;

    @Builder
    public WorkoutSet(PerformedExercise performedExercise, int setNumber, int weight, int reps) {
        this.performedExercise = performedExercise;
        this.setNumber = setNumber;
        this.weight = weight;
        this.reps = reps;
        // 휴식시간 default 0 < 추후에 develop 되면 사용
        this.restTime = 0;
    }
}
