package com.app.bdink.openai.service;

import com.app.bdink.openai.dto.response.PgvectorSearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PgvectorEmbeddingStore {
    private final @Qualifier("pgvectorJdbcTemplate") JdbcTemplate jdbcTemplate;

    public void upsertEmbedding(Long exerciseId, String embeddingText, List<Double> vector) {
        // pgvector 저장은 벡터가 존재할 때만 수행
        if (vector == null || vector.isEmpty()) {
            return;
        }

        try {
            String sql = """
                    insert into exercise_embedding (exercise_id, embedding_text, embedding_vector, createdAt, updatedAt)
                    values (?, ?, ?::vector, now(), now())
                    on conflict (exercise_id)
                    do update set
                        embedding_text = excluded.embedding_text,
                        embedding_vector = excluded.embedding_vector,
                        updatedAt = now()
                    """;

            String vectorLiteral = toVectorLiteral(vector);
            jdbcTemplate.update(sql, exerciseId, embeddingText, vectorLiteral);
        } catch (Exception e) {
            log.warn("pgvector 업서트 실패", e);
        }
    }

    public List<PgvectorSearchResult> searchTopN(List<Double> vector, int topN) {
        // 검색 벡터가 없으면 빈 결과 반환
        if (vector == null || vector.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            String sql = """
                    select exercise_id, (embedding_vector <=> ?::vector) as distance
                    from exercise_embedding
                    order by embedding_vector <=> ?::vector
                    limit ?
                    """;

            String vectorLiteral = toVectorLiteral(vector);
            return jdbcTemplate.query(
                    sql,
                    (rs, rowNum) -> new PgvectorSearchResult(
                            rs.getLong("exercise_id"),
                            rs.getDouble("distance")
                    ),
                    vectorLiteral,
                    vectorLiteral,
                    topN
            );
        } catch (Exception e) {
            log.warn("pgvector 검색 실패", e);
            return Collections.emptyList();
        }
    }

    private String toVectorLiteral(List<Double> vector) {
        // pgvector 입력 형식: [1.0,2.0,3.0]
        return vector.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));
    }
}
