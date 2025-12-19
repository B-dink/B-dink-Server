package com.app.bdink.openai.service;

import com.app.bdink.workout.entity.Exercise;
import com.app.bdink.workout.entity.ExerciseAlias;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExerciseEmbeddingTextService {

    public String buildEmbeddingText(Exercise exercise) {
        // 수동 alias + 자동 alias를 합쳐 임베딩 텍스트 구성
        Set<String> aliases = new LinkedHashSet<>();

        if (exercise.getName() != null) {
            // 이름 기반 자동 alias는 항상 생성 (수동 alias가 없어도 동작)
            aliases.addAll(generateAutoAliases(exercise.getName()));
        }

        if (exercise.getAliases() != null) {
            // 수동 alias가 없으면 이 블록은 자연스럽게 스킵됨
            for (ExerciseAlias alias : exercise.getAliases()) {
                if (alias.getAlias() != null) {
                    aliases.add(normalize(alias.getAlias()));
                }
            }
        }

        StringBuilder builder = new StringBuilder();

        if (exercise.getName() != null) {
            // name은 항상 포함해 기본 검색 힌트를 확보
            builder.append("name: ").append(exercise.getName());
        }

        if (!aliases.isEmpty()) {
            // 수동/자동 alias가 한 개도 없으면 aliases 라인은 생략
            if (!builder.isEmpty()) {
                builder.append('\n');
            }
            builder.append("aliases: ").append(String.join(", ", aliases));
        }

        if (exercise.getPart() != null) {
            if (!builder.isEmpty()) {
                builder.append('\n');
            }
            builder.append("part: ").append(exercise.getPart().name());
        }

        if (exercise.getDescription() != null && !exercise.getDescription().isBlank()) {
            if (!builder.isEmpty()) {
                builder.append('\n');
            }
            builder.append("description: ").append(exercise.getDescription());
        }

        return builder.toString();
    }

    private Set<String> generateAutoAliases(String name) {
        // 공백 제거/하이픈 제거/접미어 축약 등 기본 규칙 기반 alias 생성
        Set<String> aliases = new LinkedHashSet<>();

        String normalized = normalize(name);
        if (!normalized.isBlank()) {
            aliases.add(normalized);
        }

        String noSpaces = normalized.replace(" ", "");
        if (!noSpaces.isBlank()) {
            aliases.add(noSpaces);
        }

        String noHyphen = normalized.replace("-", "");
        if (!noHyphen.isBlank()) {
            aliases.add(noHyphen);
        }

        if (normalized.endsWith("다운") && normalized.length() > 2) {
            aliases.add(normalized.substring(0, normalized.length() - 2));
        }

        return aliases.stream()
                .filter(v -> v != null && !v.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private String normalize(String value) {
        // 소문자화 + 공백 정규화
        String trimmed = value.trim().toLowerCase();
        return trimmed.replaceAll("\\s+", " ");
    }
}
