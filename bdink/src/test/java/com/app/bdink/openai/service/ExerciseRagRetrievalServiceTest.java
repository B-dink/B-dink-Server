package com.app.bdink.openai.service;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExerciseRagRetrievalServiceTest {

    @Test
    void normalizeQueryTextRemovesNumbersAndUnits() throws Exception {
        // 메모 텍스트에서 숫자/단위를 제거해 운동명만 남기는지 확인
        ExerciseRagRetrievalService service = new ExerciseRagRetrievalService(null, null);

        Method method = ExerciseRagRetrievalService.class.getDeclaredMethod("normalizeQueryText", String.class);
        method.setAccessible(true);

        String input = "랫풀 / 40 50 55 60 60 / 10 10 10 10 10";
        String normalized = (String) method.invoke(service, input);

        assertEquals("랫풀", normalized);
    }
}
