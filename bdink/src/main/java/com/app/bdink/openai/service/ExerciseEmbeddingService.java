package com.app.bdink.openai.service;

import com.app.bdink.openai.entity.ExerciseEmbedding;
import com.app.bdink.openai.repository.ExerciseEmbeddingRepository;
import com.app.bdink.workout.entity.Exercise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseEmbeddingService {
    private final ExerciseEmbeddingRepository exerciseEmbeddingRepository;
    private final ExerciseEmbeddingTextService exerciseEmbeddingTextService;

    public void upsertEmbedding(Exercise exercise) {
        // Exercise 정보를 임베딩 텍스트로 변환
        String embeddingText = exerciseEmbeddingTextService.buildEmbeddingText(exercise);

        // 기존 데이터가 있으면 업데이트, 없으면 신규 생성
        ExerciseEmbedding embedding = exerciseEmbeddingRepository.findByExerciseId(exercise.getId())
                .orElseGet(() -> ExerciseEmbedding.builder()
                        .exerciseId(exercise.getId())
                        .build());

        // TODO: OpenAI embeddings API 호출 후 embeddingVector 저장
        // 임베딩 텍스트만 저장하고 벡터는 추후 연동
        // 벡터가 null이면 검색에서는 제외하거나 후처리 대상이 됨
        embedding.updateEmbedding(embeddingText, null);
        exerciseEmbeddingRepository.save(embedding);
    }
}
