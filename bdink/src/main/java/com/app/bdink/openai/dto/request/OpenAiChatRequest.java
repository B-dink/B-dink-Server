package com.app.bdink.openai.dto.request;

import com.app.bdink.openai.dto.OpenAiMessage;

import java.util.List;

public record OpenAiChatRequest(
        String model,
        List<OpenAiMessage> messages
) {
}
