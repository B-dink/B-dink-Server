package com.app.bdink.qna.controller.dto.response;

import com.app.bdink.qna.entity.Answer;
import com.app.bdink.qna.entity.Question;
import java.util.List;

public record QnAResponse(
    String questionContent,
    List<AnswerDto> answerContent
) {
    public static QnAResponse toEntity(Question question) {
        return new QnAResponse(question.getContent(), question.getAnswers().stream()
            .map(answer -> new AnswerDto(answer.getId(), answer.getContent()))
            .toList());
    }

}
