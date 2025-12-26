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
        // 메모에서 운동 키워드만 추출해 임베딩 (숫자/단위 제거)
        String queryText = normalizeQueryText(memoText);
        List<Double> vector = openAiEmbeddingClient.embedText(queryText);
        return pgvectorEmbeddingStore.searchTopN(vector, topN);
    }

    private String normalizeQueryText(String memoText) {
        if (memoText == null || memoText.isBlank()) {
            return "";
        }

        // 줄 단위로 분리 후 숫자/단위 제거
        String[] lines = memoText.split("\\R");
        StringBuilder builder = new StringBuilder();

        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }

            // 숫자, 한글, 단위 제거 로직
            String cleaned = line;
            cleaned = cleaned.replaceAll("\\d+(?:\\.\\d+)?", " ");
            cleaned = cleaned.replaceAll("(?i)\\b(kg|kgs|lb|lbs|reps?|sets?|set|sec|secs|seconds|mins?|minutes)\\b", " ");
            cleaned = cleaned.replaceAll("(회|세트|셋트|킬로|키로|분|초)", " ");
            cleaned = cleaned.replaceAll("[xX*/\\\\:-]", " ");
            cleaned = cleaned.replaceAll("[()\\[\\],.]+", " ");
            cleaned = cleaned.replaceAll("\\s+", " ").trim();

            if (!cleaned.isBlank()) {
                if (!builder.isEmpty()) {
                    builder.append(' ');
                }
                builder.append(cleaned);
            }
        }

        return builder.toString();
    }
}
