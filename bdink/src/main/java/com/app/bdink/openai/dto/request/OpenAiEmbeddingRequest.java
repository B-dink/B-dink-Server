package com.app.bdink.openai.dto.request;

// Embeddings API 요청 바디
public record OpenAiEmbeddingRequest(
        String model,
        String input
) {
}
