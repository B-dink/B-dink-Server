package com.app.bdink.openai.dto.response;

// pgvector 검색 결과
public record PgvectorSearchResult(
        Long exerciseId,
        double distance
) {
}
