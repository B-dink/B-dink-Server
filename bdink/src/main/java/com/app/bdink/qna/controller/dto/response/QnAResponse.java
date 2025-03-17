package com.app.bdink.qna.controller.dto.response;

import com.app.bdink.qna.entity.Answer;
import com.app.bdink.qna.entity.Question;
import java.util.List;

public record QnAResponse(
    QuestionResponse questionResponse,
    List<AnswerDto> answerContent
) {
    public static QnAResponse from(final Question question) {
        return new QnAResponse(
                QuestionResponse.from(question),
                question.getAnswers().stream()
                        .map(AnswerDto::from)
                        .toList());
    }

}
