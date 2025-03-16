package com.app.bdink.qna.controller.dto.response;

import com.app.bdink.qna.entity.Answer;

public record AnswerDto(
    Long id,
    String content
) {
    public AnswerDto from(Answer answer) {
        return new AnswerDto(answer.getId(), answer.getContent());
    }
}
