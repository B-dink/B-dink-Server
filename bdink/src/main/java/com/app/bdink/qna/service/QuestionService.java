package com.app.bdink.qna.service;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.qna.controller.dto.response.AnswerDto;
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
    public String createQuestion(final ClassRoom classRoom, QnARequest qnARequest) {
    Question question = Question.builder()
            .classRoom(classRoom)
            .content(qnARequest.content())
            .build();
        return String.valueOf(questionRepository.save(question).getId());
    }

    public List<QuestionResponse> getAllQuestions(final ClassRoom classRoom) {
        List<Question> questionList = questionRepository.findByClassRoom(classRoom);
        return questionList.stream()
            .map(QuestionResponse::new)
            .collect(Collectors.toList());
    }

    public QnAResponse getQuestionAnswer(Long questionId) {
        Question question = getById(questionId);

        return QnAResponse.from(question);
    }

    @Transactional
    public void updateQuestion(Long questionId, QnARequest qnARequest) {
        // TODO: 로그인 한 사용자와 질문 작성자가 같은지 확인
        Question question = getById(questionId);

        question.update(qnARequest.content());
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        // TODO: 로그인 한 사용자와 질문 작성자가 같은지 확인
        Question question = getById(questionId);

        questionRepository.delete(question);
    }

    public Question getById(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(
            () -> new IllegalArgumentException("해당 질문을 찾지 못했습니다.")
        );
    }
}
