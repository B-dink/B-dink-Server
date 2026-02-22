package com.app.bdink.qna.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.qna.controller.dto.request.QnARequest;
import com.app.bdink.qna.entity.Answer;
import com.app.bdink.qna.entity.Question;
import com.app.bdink.qna.entity.Status;
import com.app.bdink.qna.repository.AnswerRepository;
import com.app.bdink.qna.repository.QuestionRepository;
import com.app.bdink.notification.entity.NotificationLinkType;
import com.app.bdink.notification.entity.NotificationType;
import com.app.bdink.notification.service.NotificationFactory;
import com.app.bdink.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final NotificationService notificationService;
    private final NotificationFactory notificationFactory;

    public Answer getById(Long answerId) {

        return answerRepository.findById(answerId).orElseThrow(
            () -> new CustomException(Error.NOT_FOUND_ANSWER, Error.NOT_FOUND_ANSWER.getMessage())
        );
    }

    @Transactional
    public String createAnswer(final Question question, final QnARequest qnARequest) {
        Answer answer = Answer.builder()
            .content(qnARequest.content())
            .question(question)
            .build();

        question.updateStatus(Status.COMPLETE);
        answerRepository.save(answer);
        notificationService.create(notificationFactory.create(
                question.getMember().getId(),
                NotificationType.INSTRUCTOR_ANSWER,
                "강사 답변 등록",
                "작성한 질문에 답변이 등록되었습니다.",
                NotificationLinkType.QNA_DETAIL,
                question.getClassRoom().getId()
        ));

        return String.valueOf(answerRepository.save(answer).getId());
    }

    @Transactional
    public void updateAnswer(Long answerId, QnARequest qnARequest) {
        Answer answer = getById(answerId);
        answer.update(qnARequest.content());
    }

    @Transactional
    public void deleteAnswer(Long answerId) {
        Answer answer = getById(answerId);

        //question status -> IN_PROGRESS
        Question question = answer.getQuestion();
        question.updateStatus(Status.IN_PROGRESS);
        questionRepository.save(question);

        answerRepository.delete(answer);
    }
}
