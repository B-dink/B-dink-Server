package com.app.bdink.workout.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "performedExercise",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<WorkoutSet> workoutSets = new ArrayList<>();

    @Builder
    public PerformedExercise(WorkOutSession workOutSession, Exercise exercise) {
        this.workOutSession = workOutSession;
        this.exercise = exercise;
    }

    public void addWorkoutSet(WorkoutSet set){
        this.workoutSets.add(set);
    }

    public void clearWorkoutSets(){
        this.workoutSets.clear();
    }
}
