package com.app.bdink.openai.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "exercise_embedding")
public class ExerciseEmbedding extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // 원본 Exercise 식별자
    @Column(name = "exercise_id", nullable = false)
    private Long exerciseId;

    // 임베딩용 텍스트(원문)
    @Lob
    @Column(name = "embedding_text")
    private String embeddingText;

    // pgvector 환경에서는 마이그레이션으로 vector 컬럼으로 교체
    @Lob
    @Column(name = "embedding_vector")
    private String embeddingVector;

    @Builder
    public ExerciseEmbedding(Long exerciseId, String embeddingText, String embeddingVector) {
        this.exerciseId = exerciseId;
        this.embeddingText = embeddingText;
        this.embeddingVector = embeddingVector;
    }

    // 텍스트/벡터 갱신
    public void updateEmbedding(String embeddingText, String embeddingVector) {
        // 벡터 저장은 추후 pgvector 연동 시 채워짐
        this.embeddingText = embeddingText;
        this.embeddingVector = embeddingVector;
    }
}
