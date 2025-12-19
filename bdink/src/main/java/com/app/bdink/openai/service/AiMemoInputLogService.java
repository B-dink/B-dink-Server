package com.app.bdink.openai.service;

import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import com.app.bdink.openai.entity.AiMemoInputLog;
import com.app.bdink.openai.parser.AiParsedWorkoutDto;
import com.app.bdink.openai.repository.AiMemoInputLogRepository;
import com.app.bdink.workout.controller.dto.response.WorkoutDailyExerciseResDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AiMemoInputLogService {
    // 기본 마스킹 패턴: 이메일/전화번호
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("\\b01[016789]-?\\d{3,4}-?\\d{4}\\b");

    private final AiMemoInputLogRepository aiMemoInputLogRepository;
    private final ObjectMapper objectMapper;

    public void logSuccess(
            Member member,
            String rawText,
            AiParsedWorkoutDto parsed,
            List<WorkoutDailyExerciseResDto> matchedExercises
    ) {
        // 성공 로그: 마스킹 + 해시 + 파싱 결과 + 매칭 결과 저장
        String masked = maskSensitiveText(rawText);
        String hash = hashText(rawText);

        String parsedJson = writeJsonSafely(parsed);
        String matchedIdsJson = writeJsonSafely(
                matchedExercises.stream()
                        .map(WorkoutDailyExerciseResDto::exerciseId)
                        .toList()
        );

        AiMemoInputLog log = AiMemoInputLog.builder()
                .member(member)
                .rawTextMasked(masked)
                .rawTextHash(hash)
                .parsedResultJson(parsedJson)
                .matchedExerciseIdsJson(matchedIdsJson)
                .successFlag(true)
                .build();

        aiMemoInputLogRepository.save(log);
    }

    public void logFailure(Member member, String rawText, Error error) {
        // 실패 로그: 마스킹 + 해시 + 에러 코드 저장
        String masked = maskSensitiveText(rawText);
        String hash = hashText(rawText);

        AiMemoInputLog log = AiMemoInputLog.builder()
                .member(member)
                .rawTextMasked(masked)
                .rawTextHash(hash)
                .successFlag(false)
                .errorCode(error.name())
                .build();

        aiMemoInputLogRepository.save(log);
    }

    private String writeJsonSafely(Object value) {
        // 직렬화 실패 시 로그 저장을 막지 않도록 null 반환
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            return null;
        }
    }

    private String maskSensitiveText(String rawText) {
        // 민감정보 마스킹(이메일/전화번호)만 수행
        if (rawText == null) {
            return null;
        }

        String masked = EMAIL_PATTERN.matcher(rawText).replaceAll("[email]");
        masked = PHONE_PATTERN.matcher(masked).replaceAll("[phone]");
        return masked;
    }

    private String hashText(String rawText) {
        // 원문 해시(SHA-256) 생성
        if (rawText == null) {
            return null;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(rawText.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(encodedHash.length * 2);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
