package com.app.bdink.qna.controller.dto.response;

import com.app.bdink.qna.entity.Question;

public record QuestionResponse(
    String content
) {
    public QuestionResponse(Question question) {
        this(question.getContent());
    }
}
