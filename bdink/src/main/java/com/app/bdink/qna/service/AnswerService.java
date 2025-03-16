package com.app.bdink.qna.service;

import com.app.bdink.member.entity.Member;
import com.app.bdink.qna.controller.dto.request.QnARequest;
import com.app.bdink.qna.entity.Answer;
import com.app.bdink.qna.entity.Question;
import com.app.bdink.qna.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer getById(Long answerId) {
        return answerRepository.findById(answerId).orElseThrow(
            () -> new IllegalStateException("해당 답변을 찾지 못했습니다.")
        );
    }

    @Transactional
    public String createAnswer(final Question question, final QnARequest qnARequest) {
        // TODO: 로그인한 사용자가 관리자인지 확인
        Answer answer = Answer.builder()
            .content(qnARequest.content())
            .question(question)
            .build();
        return String.valueOf(answerRepository.save(answer).getId());
    }

    @Transactional
    public void updateAnswer(Long answerId, QnARequest qnARequest) {
        // TODO: 로그인한 사용자가 관리자인지 확인
        Answer answer = getById(answerId);
        answer.update(qnARequest.content());
    }

    @Transactional
    public void deleteAnswer(Long answerId) {
        // TODO: 로그인한 사용자가 관리자인지 확인
        Answer answer = getById(answerId);
        answerRepository.delete(answer);
    }
}
