package com.app.bdink.workout.repository;

import com.app.bdink.workout.entity.WorkoutFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkoutFeedbackRepository extends JpaRepository<WorkoutFeedback, Long> {
    Optional<WorkoutFeedback> findByWorkOutSessionId(Long workOutSessionId);
}
