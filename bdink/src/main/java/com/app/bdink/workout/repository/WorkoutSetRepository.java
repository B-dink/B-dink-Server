package com.app.bdink.workout.repository;

import com.app.bdink.workout.entity.PerformedExercise;
import com.app.bdink.workout.entity.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long> {
    List<WorkoutSet> findByPerformedExercise(PerformedExercise performedExercise);

    void deleteByPerformedExercise(PerformedExercise performedExercise);
}
