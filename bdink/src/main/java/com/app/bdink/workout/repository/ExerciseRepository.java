package com.app.bdink.workout.repository;

import com.app.bdink.workout.controller.dto.ExercisePart;
import com.app.bdink.workout.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    // 부위별 운동 종목 조회
    List<Exercise> findAllByPart(ExercisePart part);
}
