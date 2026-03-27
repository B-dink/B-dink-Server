package com.app.bdink.learning.controller;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.learning.controller.dto.request.LearningProgressRequest;
import com.app.bdink.learning.service.LearningProgressService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.member.util.MemberUtilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lecture")
@Tag(name = "학습 진행률 API", description = "강의 학습 진행률과 관련된 API들입니다.")
public class LearningProgressController {

    private final LearningProgressService learningProgressService;
    private final MemberUtilService memberUtilService;
    private final MemberService memberService;

    @GetMapping("/{lectureId}/progress")
    @Operation(method = "GET", description = "특정 강의의 학습 진행률을 조회합니다.")
    public RspTemplate<?> getLearningProgress(Principal principal, @PathVariable Long lectureId) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        return RspTemplate.success(
                Success.GET_LEARNING_PROGRESS_SUCCESS,
                learningProgressService.getLearningProgress(member, lectureId)
        );
    }

    @PostMapping("/{lectureId}/progress")
    @Operation(method = "POST", description = "특정 강의의 학습 진행률을 저장합니다.")
    public RspTemplate<?> saveLearningProgress(Principal principal, @PathVariable Long lectureId,
                                               @RequestBody LearningProgressRequest request) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        return RspTemplate.success(
                Success.UPDATE_LEARNING_PROGRESS_SUCCESS,
                learningProgressService.saveLearningProgress(member, lectureId, request)
        );
    }
}
