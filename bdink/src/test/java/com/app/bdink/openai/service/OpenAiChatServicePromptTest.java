package com.app.bdink.openai.service;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenAiChatServicePromptTest {

    @Test
    void buildPromptIncludesExerciseIdAndRagHints() throws Exception {
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
}
