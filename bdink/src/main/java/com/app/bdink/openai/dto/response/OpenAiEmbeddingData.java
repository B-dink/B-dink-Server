package com.app.bdink.openai.dto.response;

import java.util.List;

// Embeddings API 응답의 개별 데이터
public record OpenAiEmbeddingData(
        List<Double> embedding
) {
}
