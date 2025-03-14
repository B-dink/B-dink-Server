package com.app.bdink.question.service;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.question.controller.dto.request.QuestionRequest;
import com.app.bdink.question.controller.dto.response.QuestionResponse;
import com.app.bdink.question.entity.Question;
import com.app.bdink.question.repository.QuestionRepository;
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
    public String createQuestion(final ClassRoom classRoom, QuestionRequest questionRequest) {
        Question question = Question.builder()
            .classRoom(classRoom)
            .content(questionRequest.content())
            .build();
        return String.valueOf(questionRepository.save(question).getId());
    }

    public List<QuestionResponse> getAllQuestions(final ClassRoom classRoom) {
        List<Question> questionList = questionRepository.findByClassRoom(classRoom);
        return questionList.stream()
            .map(QuestionResponse::new)
            .collect(Collectors.toList());
    }

    public QuestionResponse getQuestionDetail(Long questionId) {
        Question question = getById(questionId);
        return new QuestionResponse(question);
    }

    @Transactional
    public void updateQuestion(Long questionId, QuestionRequest questionRequest) {
        Question question = getById(questionId);

        question.update(questionRequest.content());
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        Question question = getById(questionId);

        questionRepository.delete(question);
    }

    public Question getById(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(
            () -> new IllegalArgumentException("해당 질문을 찾지 못했습니다.")
        );
    }
}
