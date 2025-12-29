package com.app.bdink.openai.service;

import com.app.bdink.openai.dto.response.PgvectorSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<PgvectorSearchResult> retrieveTopCandidatesByLine(String memoText, int topNPerLine) {
        // 줄 단위로 분리해 각 줄별로 후보를 수집한 뒤 합친다
        List<List<PgvectorSearchResult>> grouped = retrieveTopCandidatesByLineGrouped(memoText, topNPerLine);
        Map<Long, PgvectorSearchResult> bestById = new HashMap<>();

        for (List<PgvectorSearchResult> results : grouped) {
            for (PgvectorSearchResult result : results) {
                if (result == null || result.exerciseId() == null) {
                    continue;
                }
                PgvectorSearchResult existing = bestById.get(result.exerciseId());
                if (existing == null || result.distance() < existing.distance()) {
                    bestById.put(result.exerciseId(), result);
                }
            }
        }

        return bestById.values().stream()
                .sorted(Comparator.comparingDouble(PgvectorSearchResult::distance))
                .collect(Collectors.toList());
    }

    public List<List<PgvectorSearchResult>> retrieveTopCandidatesByLineGrouped(String memoText, int topNPerLine) {
        if (memoText == null || memoText.isBlank()) {
            return List.of();
        }

        List<String> lines = splitMemoLines(memoText);
        List<List<PgvectorSearchResult>> grouped = new ArrayList<>();

        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }

            String cleaned = normalizeLine(line);
            if (cleaned.isBlank()) {
                continue;
            }

            List<Double> vector = openAiEmbeddingClient.embedText(cleaned);
            List<PgvectorSearchResult> results = pgvectorEmbeddingStore.searchTopN(vector, topNPerLine);
            grouped.add(results);
        }

        return grouped;
    }

    public List<String> splitMemoLinesForParsing(String memoText) {
        return splitMemoLines(memoText);
    }

    private String normalizeQueryText(String memoText) {
        if (memoText == null || memoText.isBlank()) {
            return "";
        }

        // 줄 단위로 분리 후 숫자/단위 제거
        List<String> lines = splitMemoLines(memoText);
        StringBuilder builder = new StringBuilder();

        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }

            String cleaned = normalizeLine(line);

            if (!cleaned.isBlank()) {
                if (!builder.isEmpty()) {
                    builder.append(' ');
                }
                builder.append(cleaned);
            }
        }

        return builder.toString();
    }

    private List<String> splitMemoLines(String memoText) {
        // 1) 명시적 구분자 우선 분리 (\n, ;, |, ,)
        String normalized = memoText.replace("\r\n", "\n").replace("\r", "\n");
        String[] rawParts = normalized.split("[\\n;|,]+");
        List<String> parts = new ArrayList<>();

        for (String part : rawParts) {
            if (part == null) {
                continue;
            }
            String trimmed = part.trim();
            if (trimmed.isBlank()) {
                continue;
            }

            // 2) 숫자가 있는 경우에만 숫자 블록 이후 새 단어로 분리
            if (trimmed.matches(".*\\d+.*")) {
                parts.addAll(splitByNumberBoundary(trimmed));
            } else {
                parts.add(trimmed);
            }
        }

        return parts;
    }

    private List<String> splitByNumberBoundary(String text) {
        // 숫자 블록 뒤에 문자가 시작되면 새로운 운동으로 분리
        List<String> parts = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        boolean seenNumber = false;
        boolean inNumberRun = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (Character.isDigit(c)) {
                seenNumber = true;
                inNumberRun = true;
                builder.append(c);
                continue;
            }

            if (inNumberRun) {
                if (isSplitBoundaryChar(c)) {
                    builder.append(c);
                    continue;
                }
                if (isLetterOrKorean(c)) {
                    // 숫자 블록이 끝나고 새로운 단어가 시작되면 분리
                    String current = builder.toString().trim();
                    if (!current.isBlank()) {
                        parts.add(current);
                    }
                    builder.setLength(0);
                    builder.append(c);
                    inNumberRun = false;
                    continue;
                }
                inNumberRun = false;
            }

            builder.append(c);
        }

        String tail = builder.toString().trim();
        if (!tail.isBlank()) {
            parts.add(tail);
        }

        return seenNumber ? parts : List.of(text.trim());
    }

    private boolean isSplitBoundaryChar(char c) {
        return Character.isWhitespace(c)
                || c == '/'
                || c == 'x'
                || c == 'X'
                || c == '*'
                || c == '-'
                || c == ':';
    }

    private boolean isLetterOrKorean(char c) {
        if (Character.isLetter(c)) {
            return true;
        }
        // Hangul syllables
        return c >= 0xAC00 && c <= 0xD7A3;
    }

    private String normalizeLine(String line) {
        // 숫자, 단위 제거 로직
        String cleaned = line;
        cleaned = cleaned.replaceAll("\\d+(?:\\.\\d+)?", " ");
        cleaned = cleaned.replaceAll("(?i)\\b(kg|kgs|lb|lbs|reps?|sets?|set|sec|secs|seconds|mins?|minutes)\\b", " ");
        cleaned = cleaned.replaceAll("(회|세트|셋트|킬로|키로|분|초)", " ");
        cleaned = cleaned.replaceAll("[xX*/\\\\:-]", " ");
        cleaned = cleaned.replaceAll("[()\\[\\],.]+", " ");
        cleaned = cleaned.replaceAll("\\s+", " ").trim();
        return cleaned;
    }
}
