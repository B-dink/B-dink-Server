package com.app.bdink.qna.service;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import com.app.bdink.qna.entity.Question;
import com.app.bdink.qna.controller.dto.request.QnARequest;
import com.app.bdink.qna.controller.dto.response.QnAResponse;
import com.app.bdink.qna.controller.dto.response.QuestionResponse;
import com.app.bdink.qna.repository.QuestionRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public String createQuestion(final Member member, final ClassRoomEntity classRoom, QnARequest qnARequest) {
        Question question = Question.builder()
            .member(member)
            .classRoom(classRoom)
            .content(qnARequest.content())
            .build();
        return String.valueOf(questionRepository.save(question).getId());
    }

    public List<QuestionResponse> getAllQuestions(final ClassRoomEntity classRoomEntity) {
        List<Question> questionList = questionRepository.findByClassRoom(classRoomEntity);
        return questionList.stream()
            .map(QuestionResponse::from)
            .collect(Collectors.toList());
    }

    public QnAResponse getQuestionAnswer(Long questionId) {
        Question question = getById(questionId);

        return QnAResponse.from(question);
    }

    @Transactional
    public void updateQuestion(final Member member,Long questionId, QnARequest qnARequest) {
        validateQuestion(member, questionId);
        Question question = getById(questionId);

        question.update(qnARequest.content());
    }

    @Transactional
    public void deleteQuestion(final Member member,Long questionId) {
        validateQuestion(member, questionId);
        Question question = getById(questionId);

        questionRepository.delete(question);
    }

    public Question getById(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(
            () -> new CustomException(Error.NOT_FOUND_QUESTION, Error.NOT_FOUND_QUESTION.getMessage())
        );
    }

    public void validateQuestion(final Member member, Long questionId) {
        if (!member.equals(getById(questionId).getMember())) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }
    }
}
