package com.app.bdink.qna.controller;

import com.app.bdink.classroom.util.InstructorUtilService;
import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.qna.controller.dto.request.QnARequest;
import com.app.bdink.qna.entity.Question;
import com.app.bdink.qna.service.AnswerService;
import com.app.bdink.qna.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/answers")
@Tag(name = "답변 API", description = "질문에 대한 답변과 관련된 API들입니다. 답변은 관리자만 관리할 수 있습니다.")
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;
    private final InstructorUtilService instructorUtilService;

    @Operation(method = "POST", description = "답변을 등록합니다.")
    @PostMapping
    public RspTemplate<?> createAnswer(
            Principal principal,
            @RequestParam Long questionId,
            @RequestBody QnARequest qnARequest) {

        Question question = questionService.getById(questionId);

        if (!instructorUtilService.validateAccessAnswer(principal, question.getClassRoom())) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }

        return RspTemplate.success(Success.CREATE_ANSWER_SUCCESS, CreateIdDto.from(answerService.createAnswer(question, qnARequest)));
    }

    @Operation(method = "PUT", description = "답변을 수정합니다.")
    @PutMapping
    public RspTemplate<?> updateAnswer(Principal principal,
                                          @RequestParam Long answerId, @RequestBody QnARequest qnARequest) {

        if (!instructorUtilService.validateAccessAnswer(principal, answerId)) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }

        answerService.updateAnswer(answerId, qnARequest);
        return RspTemplate.success(Success.UPDATE_ANSWER_SUCCESS, Success.UPDATE_ANSWER_SUCCESS.getMessage());
    }

    @Operation(method = "DELETE", description = "답변을 삭제합니다.")
    @DeleteMapping
    public RspTemplate<?> deleteAnswer(Principal principal, @RequestParam Long answerId) {

        if (!instructorUtilService.validateAccessAnswer(principal, answerId)) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }

        answerService.deleteAnswer(answerId);
        return RspTemplate.success(Success.DELETE_ANSWER_SUCCESS, Success.DELETE_ANSWER_SUCCESS.getMessage());
    }

}
