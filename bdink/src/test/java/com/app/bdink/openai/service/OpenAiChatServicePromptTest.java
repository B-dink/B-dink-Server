package com.app.bdink.openai.service;

import com.app.bdink.openai.config.OpenAiConfig;
import com.app.bdink.openai.parser.AiParsedWorkoutDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class OpenAiChatServicePromptTest {
    private static final Logger log = LoggerFactory.getLogger(OpenAiChatServicePromptTest.class);

    @Autowired
    private OpenAiChatService openAiChatService;

    @Autowired
    private OpenAiConfig openAiConfig;


    @Test
    void buildPromptIncludesExerciseIdAndRagHints() throws Exception {
        // 프롬프트에 exerciseId와 후보 리스트 문구가 포함되는지 확인
        OpenAiChatService service = new OpenAiChatService(null, null, null);

        Method method = OpenAiChatService.class.getDeclaredMethod("buildPrompt", String.class, String.class);
        method.setAccessible(true);

        String ragHints = "id:1 name:벤치프레스 part:CHEST";
        String memoText = "벤치프레스 60x10 70x8";
        String prompt = (String) method.invoke(service, memoText, ragHints);

        assertTrue(prompt.contains("exerciseId"));
        assertTrue(prompt.contains("후보 운동 리스트"));
        assertTrue(prompt.contains(ragHints));
    }

    @Test
    void logOpenAiParsedResult() throws Exception {
        // 실제 OpenAI 호출 결과(JSON 파싱 결과)를 로그로 확인
        Assumptions.assumeTrue(
                openAiConfig.getApiKey() != null && !openAiConfig.getApiKey().isBlank(),
                "OPENAI_API_KEY required"
        );
        Assumptions.assumeTrue(
                openAiConfig.getChatModel() != null && !openAiConfig.getChatModel().isBlank(),
                "OPENAI_CHAT_MODEL required"
        );

        String ragHints = "id:4 name:데드리프트 part:BACK aliases:데드리프트, 데드 리프트";
        String memoText = "데드 / 70 80 90 100 110 / 10 10 10 10 15";

        AiParsedWorkoutDto parsed = openAiChatService.parsedWorkoutDtoDto(memoText, ragHints);
        log.info("OpenAI parsed result: {}", new ObjectMapper().writeValueAsString(parsed));
    }
}
