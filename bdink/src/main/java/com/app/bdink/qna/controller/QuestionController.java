package com.app.bdink.qna.controller;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.qna.controller.dto.request.QnARequest;
import com.app.bdink.qna.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createQuestion(@RequestParam Long classRoomId,
                                            @RequestBody QnARequest qnARequest) {
        ClassRoom classRoom = classRoomService.findById(classRoomId);
        String id = questionService.createQuestion(classRoom, qnARequest);
        return ResponseEntity.created(
                URI.create(id))
            .build();
    }

    @Operation(method = "GET", description = "클래스룸 별 모든 질문을 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getAllQuestions(@RequestParam Long classRoomId) {
        ClassRoom classRoom = classRoomService.findById(classRoomId);
        return ResponseEntity.ok().body(questionService.getAllQuestions(classRoom));
    }

    @Operation(method = "GET", description = "질문과 답변을 조회합니다.")
    @GetMapping("/detail")
    public ResponseEntity<?> getQuestionAnswer(@RequestParam Long questionId) {
        return ResponseEntity.ok().body(questionService.getQuestionAnswer(questionId));
    }

    @Operation(method = "PUT", description = "질문을 수정합니다.")
    @PutMapping
    public ResponseEntity<?> updateQuestion(@RequestParam Long questionId, @RequestBody QnARequest qnARequest) {
        questionService.updateQuestion(questionId, qnARequest);
        return ResponseEntity.ok().build();
    }

    @Operation(method = "DELETE", description = "질문을 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<?> deleteQuestion(@RequestParam Long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok().build();
    }
}
