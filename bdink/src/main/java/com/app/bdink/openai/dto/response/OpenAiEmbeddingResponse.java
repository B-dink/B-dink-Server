package com.app.bdink.openai.dto.response;

import java.util.List;

// Embeddings API 응답 바디
public record OpenAiEmbeddingResponse(
        List<OpenAiEmbeddingData> data
) {
}
