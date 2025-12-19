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

        for (Exercise exercise : exercises) {
            if (exerciseEmbeddingRepository.existsByExerciseId(exercise.getId())) {
                // 이미 생성된 임베딩은 중복 생성하지 않음
                continue;
            }
            exerciseEmbeddingService.upsertEmbedding(exercise);
            created++;
        }

        // 실행 횟수/생성량 확인용 로그
        log.info("Exercise embedding backfill done. created={}", created);
    }
}
