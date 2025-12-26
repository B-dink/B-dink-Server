package com.app.bdink.openai.service;

import com.app.bdink.openai.dto.response.PgvectorSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseRagRetrievalService {
    private final OpenAiEmbeddingClient openAiEmbeddingClient;
    private final PgvectorEmbeddingStore pgvectorEmbeddingStore;

    public List<PgvectorSearchResult> retrieveTopCandidates(String memoText, int topN) {
        // 메모 텍스트를 임베딩해서 pgvector에서 후보 검색
        List<Double> vector = openAiEmbeddingClient.embedText(memoText);
        return pgvectorEmbeddingStore.searchTopN(vector, topN);
    }
}
