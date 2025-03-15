package com.app.bdink.question.controller.dto.response;

import com.app.bdink.question.entity.Question;

public record QuestionResponse(
    String content
) {
    public QuestionResponse(Question question) {
        this(question.getContent());
    }
}
