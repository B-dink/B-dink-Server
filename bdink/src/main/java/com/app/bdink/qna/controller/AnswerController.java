package com.app.bdink.qna.controller;

import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.qna.controller.dto.request.QnARequest;
import com.app.bdink.qna.entity.Question;
import com.app.bdink.qna.service.AnswerService;
import com.app.bdink.qna.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
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

    @Operation(method = "POST", description = "답변을 등록합니다.")
    @PostMapping
    public ResponseEntity<?> createAnswer(@RequestParam Long questionId, @RequestBody
        QnARequest qnARequest) {
        Question question = questionService.getById(questionId);
        String id = answerService.createAnswer(question, qnARequest);
        return ResponseEntity.created(
            URI.create(id))
            .build();
    }

    @Operation(method = "PUT", description = "답변을 수정합니다.")
    @PutMapping
    public ResponseEntity<?> updateAnswer( @RequestParam Long answerId, @RequestBody QnARequest qnARequest) {
        answerService.updateAnswer(answerId, qnARequest);
        return ResponseEntity.ok().build();
    }

    @Operation(method = "DELETE", description = "답변을 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<?> deleteAnswer(@RequestParam Long answerId) {
        answerService.deleteAnswer(answerId);
        return ResponseEntity.noContent().build();
    }

}
