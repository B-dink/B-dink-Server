package com.app.bdink.workout.repository;

import com.app.bdink.workout.entity.ExerciseAlias;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseAliasRepository extends JpaRepository<ExerciseAlias, Long> {
    // 수동 alias 엔티티 기본 CRUD
}
