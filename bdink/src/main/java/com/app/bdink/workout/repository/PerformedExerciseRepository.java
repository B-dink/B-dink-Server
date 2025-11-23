package com.app.bdink.workout.repository;

import com.app.bdink.workout.controller.dto.request.WorkoutSessionSaveReqDto;
import com.app.bdink.workout.entity.PerformedExercise;
import com.app.bdink.workout.entity.WorkOutSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformedExerciseRepository extends JpaRepository<PerformedExercise, Long> {
    List<PerformedExercise> findByWorkOutSession(WorkOutSession workOutSession);
}
