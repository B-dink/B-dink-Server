package com.app.bdink.workout.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoutineSetTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private RoutineExercise routineExercise;

    @Column(name = "setNumber")
    private int setNumber;

    @Column(name = "weight")
    private int weight;

    @Column(name = "reps")
    private int reps;

    @Builder
    public RoutineSetTemplate(int setNumber, int reps, int weight) {
        this.setNumber = setNumber;
        this.reps = reps;
        this.weight = weight;
    }

    void setRoutineExercise(RoutineExercise routineExercise) {
        this.routineExercise = routineExercise;
    }
}
