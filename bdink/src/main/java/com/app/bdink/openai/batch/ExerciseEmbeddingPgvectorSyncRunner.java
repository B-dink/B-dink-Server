package com.app.bdink.openai.batch;

import com.app.bdink.openai.entity.ExerciseEmbedding;
import com.app.bdink.openai.repository.ExerciseEmbeddingRepository;
import com.app.bdink.openai.service.PgvectorEmbeddingStore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "ai-memo.embedding.pgvector-sync", name = "enabled", havingValue = "true")
public class ExerciseEmbeddingPgvectorSyncRunner implements CommandLineRunner {
    private final ExerciseEmbeddingRepository exerciseEmbeddingRepository;
    private final PgvectorEmbeddingStore pgvectorEmbeddingStore;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) {
        List<ExerciseEmbedding> embeddings = exerciseEmbeddingRepository.findAll();
        int synced = 0;
        int skipped = 0;

        for (ExerciseEmbedding embedding : embeddings) {
            Long exerciseId = embedding.getExerciseId();
            String vectorJson = embedding.getEmbeddingVector();

            if (exerciseId == null || vectorJson == null || vectorJson.isBlank()) {
                skipped++;
                continue;
            }

            List<Double> vector = parseVector(vectorJson, exerciseId);
            if (vector == null || vector.isEmpty()) {
                skipped++;
                continue;
            }

            pgvectorEmbeddingStore.upsertEmbedding(
                    exerciseId,
                    embedding.getEmbeddingText(),
                    vector
            );
            synced++;
        }

        log.info("Exercise embedding pgvector sync done. synced={}, skipped={}", synced, skipped);
    }

    private List<Double> parseVector(String vectorJson, Long exerciseId) {
        try {
            return objectMapper.readValue(vectorJson, new TypeReference<List<Double>>() {});
        } catch (Exception e) {
            log.warn("Failed to parse embedding vector. exerciseId={}", exerciseId, e);
            return null;
        }
    }
}
