package com.app.bdink.question.controller.dto.request;

import com.app.bdink.question.entity.Question;

public record QuestionRequest(
    String content
) {
    public Question toEntity() {
        return Question.builder()
            .content(content)
            .build();
    }
}
