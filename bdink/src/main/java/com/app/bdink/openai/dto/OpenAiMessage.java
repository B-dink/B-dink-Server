package com.app.bdink.openai.dto;

public record OpenAiMessage(
        String role,
        String content
) {
}
