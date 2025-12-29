package com.app.bdink.openai.repository;

import com.app.bdink.openai.entity.ExerciseEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseEmbeddingRepository extends JpaRepository<ExerciseEmbedding, Long> {
    // Exercise 기준으로 임베딩 레코드 조회
    Optional<ExerciseEmbedding> findByExerciseId(Long exerciseId);

    // Exercise 기준 임베딩 존재 여부
    boolean existsByExerciseId(Long exerciseId);
}
