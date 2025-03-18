package com.app.bdink.qna.controller;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.qna.controller.dto.request.QnARequest;
import com.app.bdink.qna.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
@Tag(name = "Q&A 질문 API", description = "Q&A 질문과 관련된 API들입니다.")
public class QuestionController {

    private final QuestionService questionService;
    private final ClassRoomService classRoomService;

    @Operation(method = "POST", description = "질문을 생성합니다.")
    @PostMapping
    public RspTemplate<?> createQuestion(@RequestParam Long classRoomId,
                                            @RequestBody QnARequest qnARequest) {

        ClassRoomEntity classRoomEntity = classRoomService.findById(classRoomId);
        return RspTemplate.success(Success.CREATE_ANSWER_SUCCESS, CreateIdDto.from(questionService.createQuestion(classRoomEntity, qnARequest)));
    }

    @Operation(method = "GET", description = "클래스룸 별 모든 질문을 조회합니다.")
    @GetMapping
    public RspTemplate<?> getAllQuestions(@RequestParam Long classRoomId) {
        ClassRoomEntity classRoomEntity = classRoomService.findById(classRoomId);
        return RspTemplate.success(Success.GET_QUESTION_BY_CLASS_SUCCESS, questionService.getAllQuestions(classRoomEntity));
    }

    @Operation(method = "GET", description = "질문과 답변을 조회합니다.")
    @GetMapping("/detail")
    public RspTemplate<?> getQuestionAnswer(@RequestParam Long questionId) {
        return RspTemplate.success(Success.GET_QUESTION_DETAIL_SUCCESS, questionService.getQuestionAnswer(questionId));
    }

    @Operation(method = "PUT", description = "질문을 수정합니다.")
    @PutMapping
    public RspTemplate<?> updateQuestion(@RequestParam Long questionId, @RequestBody QnARequest qnARequest) {
        questionService.updateQuestion(questionId, qnARequest);
        return RspTemplate.success(Success.UPDATE_QUESTION_SUCCESS, Success.UPDATE_QUESTION_SUCCESS.getMessage());
    }

    @Operation(method = "DELETE", description = "질문을 삭제합니다.")
    @DeleteMapping
    public RspTemplate<?> deleteQuestion(@RequestParam Long questionId) {
        questionService.deleteQuestion(questionId);
        return RspTemplate.success(Success.DELETE_QUESTION_SUCCESS, Success.DELETE_QUESTION_SUCCESS.getMessage());
    }
}
