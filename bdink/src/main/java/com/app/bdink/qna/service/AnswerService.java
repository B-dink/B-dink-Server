package com.app.bdink.qna.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.qna.controller.dto.request.QnARequest;
import com.app.bdink.qna.entity.Answer;
import com.app.bdink.qna.entity.Question;
import com.app.bdink.qna.entity.Status;
import com.app.bdink.qna.repository.AnswerRepository;
import com.app.bdink.qna.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

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
        answer.getQuestion().updateStatus(Status.IN_PROGRESS);
        questionRepository.save(answer.getQuestion());
        answerRepository.delete(answer);
    }
}
