package com.app.bdink.openai.service;

import com.app.bdink.openai.entity.ExerciseEmbedding;
import com.app.bdink.openai.repository.ExerciseEmbeddingRepository;
import com.app.bdink.workout.entity.Exercise;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExerciseEmbeddingService {
    private final ExerciseEmbeddingRepository exerciseEmbeddingRepository;
    private final ExerciseEmbeddingTextService exerciseEmbeddingTextService;
    private final OpenAiEmbeddingClient openAiEmbeddingClient;
    private final ObjectMapper objectMapper;
    private final PgvectorEmbeddingStore pgvectorEmbeddingStore;

    public void upsertEmbedding(Exercise exercise) {
        // Exercise 정보를 임베딩 텍스트로 변환
        String embeddingText = exerciseEmbeddingTextService.buildEmbeddingText(exercise);

        // 기존 데이터가 있으면 업데이트, 없으면 신규 생성
        ExerciseEmbedding embedding = exerciseEmbeddingRepository.findByExerciseId(exercise.getId())
                .orElseGet(() -> ExerciseEmbedding.builder()
                        .exerciseId(exercise.getId())
                        .build());

        // OpenAI embeddings API 호출 후 벡터를 JSON 문자열로 저장
        List<Double> vector = openAiEmbeddingClient.embedText(embeddingText);
        String embeddingVectorJson = writeVectorJsonSafely(vector);
        // 벡터가 null이면 검색에서는 제외하거나 후처리 대상이 됨
        embedding.updateEmbedding(embeddingText, embeddingVectorJson);
        exerciseEmbeddingRepository.save(embedding);

        // pgvector 저장소에도 동기화 (연결 실패 시 로깅만)
        pgvectorEmbeddingStore.upsertEmbedding(exercise.getId(), embeddingText, vector);
    }

    private String writeVectorJsonSafely(List<Double> vector) {
        try {
            if (vector == null || vector.isEmpty()) {
                return null;
            }

            return objectMapper.writeValueAsString(vector);
        } catch (Exception e) {
            return null;
        }
    }
}
