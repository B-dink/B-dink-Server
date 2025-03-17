package com.app.bdink.qna.controller.dto.response;

import com.app.bdink.qna.entity.Question;

public record QuestionResponse(
        Long id,
        String content
) {
    public static QuestionResponse from(Question question) {
        return new QuestionResponse(question.getId(), question.getContent());
    }
}
