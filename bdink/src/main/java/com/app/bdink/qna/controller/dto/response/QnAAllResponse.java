package com.app.bdink.qna.controller.dto.response;

import com.app.bdink.qna.entity.Question;
import com.app.bdink.qna.entity.Status;

import java.util.List;

public record QnAAllResponse (
    Long id,
    String content,
    Status status,
    List<AnswerDto> answers
)
{
    public static QnAAllResponse from(Question question) {
        List<AnswerDto> answerDtos = question.getAnswers().stream()
                .map(AnswerDto::from)
                .toList();

        return new QnAAllResponse(
                question.getId(),
                question.getContent(),
                question.getStatus(),
                answerDtos
        );

    }
}
