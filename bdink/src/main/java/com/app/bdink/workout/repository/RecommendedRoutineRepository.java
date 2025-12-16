package com.app.bdink.workout.repository;

import com.app.bdink.workout.entity.RecommendedRoutine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendedRoutineRepository extends JpaRepository<RecommendedRoutine, Long> {
}
