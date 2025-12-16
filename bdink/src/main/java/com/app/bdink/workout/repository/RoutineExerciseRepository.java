package com.app.bdink.workout.repository;

import com.app.bdink.workout.entity.RoutineExercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineExerciseRepository extends JpaRepository<RoutineExercise, Long> {
}
