package com.app.bdink.openai.batch;

import com.app.bdink.openai.repository.ExerciseEmbeddingRepository;
import com.app.bdink.openai.service.ExerciseEmbeddingService;
import com.app.bdink.workout.entity.Exercise;
import com.app.bdink.workout.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "ai-memo.embedding.backfill", name = "enabled", havingValue = "true")
public class ExerciseEmbeddingBackfillRunner implements CommandLineRunner {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseEmbeddingRepository exerciseEmbeddingRepository;
    private final ExerciseEmbeddingService exerciseEmbeddingService;

    @Override
    // 백필 중 insert가 발생하므로 readOnly 사용 금지
    @Transactional
    public void run(String... args) {
        // 임베딩 레코드가 없는 기존 Exercise만 1회성 백필
        List<Exercise> exercises = exerciseRepository.findAll();
        int created = 0;
        int updated = 0;
        int skipped = 0;

        for (Exercise exercise : exercises) {
            var existing = exerciseEmbeddingRepository.findByExerciseId(exercise.getId());
            if (existing.isPresent()) {
                String vector = existing.get().getEmbeddingVector();
                if (vector != null && !vector.isBlank()) {
                    // 벡터가 이미 존재하면 스킵
                    skipped++;
                    continue;
                }
                // 벡터가 비어 있으면 재생성
                exerciseEmbeddingService.upsertEmbedding(exercise);
                updated++;
                continue;
            }
            exerciseEmbeddingService.upsertEmbedding(exercise);
            created++;
        }

        // 실행 횟수/생성량 확인용 로그
        log.info("Exercise embedding backfill done. created={}, updated={}, skipped={}", created, updated, skipped);
    }
}
