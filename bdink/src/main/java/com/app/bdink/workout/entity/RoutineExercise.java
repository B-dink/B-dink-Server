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
public class RoutineExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private RecommendedRoutine routine;

    @ManyToOne(fetch = FetchType.LAZY)
    private Exercise exercise;

    @Column(name = "memo")
    private String memo;

    @Column(name = "orderIndex")
    private int orderIndex;

    @OneToMany(mappedBy = "routineExercise",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<RoutineSetTemplate> sets = new ArrayList<>();

    @Builder
    public RoutineExercise(Exercise exercise, String memo, int orderIndex) {
        this.exercise = exercise;
        this.memo = memo;
        this.orderIndex = orderIndex;
    }

    void setRoutine(RecommendedRoutine routine) {
        this.routine = routine;
    }

    public void addSet(RoutineSetTemplate set) {
        this.sets.add(set);
        set.setRoutineExercise(this);
    }
}
