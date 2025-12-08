package com.app.bdink.openai.dto.response;

import com.app.bdink.openai.dto.OpenAiChoice;

import java.util.List;

public record OpenAiChatResponse(
        List<OpenAiChoice> choices
) {
}
