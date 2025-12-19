package com.app.bdink.openai.service;

import com.app.bdink.openai.config.OpenAiConfig;
import com.app.bdink.openai.dto.request.OpenAiEmbeddingRequest;
import com.app.bdink.openai.dto.response.OpenAiEmbeddingResponse;
import com.app.bdink.openai.entity.ExerciseEmbedding;
import com.app.bdink.openai.repository.ExerciseEmbeddingRepository;
import com.app.bdink.workout.entity.Exercise;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExerciseEmbeddingService {
    private final ExerciseEmbeddingRepository exerciseEmbeddingRepository;
    private final ExerciseEmbeddingTextService exerciseEmbeddingTextService;
    private final WebClient openAiWebClient;
    private final OpenAiConfig openAiConfig;
    private final ObjectMapper objectMapper;

    public void upsertEmbedding(Exercise exercise) {
        // Exercise 정보를 임베딩 텍스트로 변환
        String embeddingText = exerciseEmbeddingTextService.buildEmbeddingText(exercise);

        // 기존 데이터가 있으면 업데이트, 없으면 신규 생성
        ExerciseEmbedding embedding = exerciseEmbeddingRepository.findByExerciseId(exercise.getId())
                .orElseGet(() -> ExerciseEmbedding.builder()
                        .exerciseId(exercise.getId())
                        .build());

        // OpenAI embeddings API 호출 후 벡터를 JSON 문자열로 저장
        String embeddingVectorJson = fetchEmbeddingVectorJson(embeddingText);
        // 벡터가 null이면 검색에서는 제외하거나 후처리 대상이 됨
        embedding.updateEmbedding(embeddingText, embeddingVectorJson);
        exerciseEmbeddingRepository.save(embedding);
    }

    private String fetchEmbeddingVectorJson(String embeddingText) {
        try {
            OpenAiEmbeddingRequest request = new OpenAiEmbeddingRequest(
                    openAiConfig.getEmbeddingModel(),
                    embeddingText
            );

            // OpenAI embedding model using
            OpenAiEmbeddingResponse response = openAiWebClient.post()
                    .uri("/embeddings")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(OpenAiEmbeddingResponse.class)
                    .block();

            if (response == null || response.data() == null || response.data().isEmpty()) {
                log.warn("OpenAI embedding 응답이 비어있음");
                return null;
            }

            List<Double> vector = response.data().get(0).embedding();
            if (vector == null || vector.isEmpty()) {
                log.warn("OpenAI embedding 벡터가 비어있음");
                return null;
            }

            return objectMapper.writeValueAsString(vector);
        } catch (Exception e) {
            log.warn("OpenAI embedding 요청 실패", e);
            return null;
        }
    }
}
