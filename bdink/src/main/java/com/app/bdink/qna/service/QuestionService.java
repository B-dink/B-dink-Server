package com.app.bdink.qna.service;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import com.app.bdink.qna.controller.dto.request.QnARequest;
import com.app.bdink.qna.controller.dto.response.QnAAllResponse;
import com.app.bdink.qna.controller.dto.response.QnAResponse;
import com.app.bdink.qna.entity.Question;
import com.app.bdink.qna.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Question findById(Long id){
        return questionRepository.findById(id).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_QUESTION, Error.NOT_FOUND_QUESTION.getMessage())
        );
    }

    @Transactional
    public String createQuestion(final Member member, final ClassRoomEntity classRoom, QnARequest qnARequest) {
        Question question = Question.builder()
            .member(member)
            .classRoom(classRoom)
            .content(qnARequest.content())
            .build();
        return String.valueOf(questionRepository.save(question).getId());
    }

    public List<QnAAllResponse> getAllQuestions(final ClassRoomEntity classRoomEntity) {
        return questionRepository.findByClassRoom(classRoomEntity).stream()
                .map(QnAAllResponse::from)
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

    @Transactional
    public void revokeUserDeleteQuestion(final Member member){
        List<Question> questions = questionRepository.findByMember(member);
        questionRepository.deleteAll(questions);
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
