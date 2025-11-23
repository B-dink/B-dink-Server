package com.app.bdink.workout.controller;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.workout.controller.dto.request.WorkoutSessionSaveReqDto;
import com.app.bdink.workout.service.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workout")
@Tag(name = "일지작성 API", description = "운동 일지작성과 관련된 API들입니다.")
public class WorkoutController {
    private final WorkoutService workoutService;
    private final MemberService memberService;
    private final MemberUtilService memberUtilService;

    @PostMapping
    @Operation(method = "POST", description = "운동일지를 작성합니다.")
    public RspTemplate<?> createExerciseList(Principal principal,
                                             @RequestBody WorkoutSessionSaveReqDto requestDto){
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        // workoutservice 계층에서 기록완료 메서드 사용
        return RspTemplate.success(Success.CREATE_EXERCISELIST_SUCCESS, workoutService.saveWorkoutSession(member, requestDto));
    }

    @DeleteMapping("/{sessionId}")
    @Operation(method = "DELETE", description = "운동일지를 삭제합니다.")
    public RspTemplate<?> deleteWorkoutSession(Principal principal, @PathVariable Long sessionId){
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        workoutService.deleteWorkoutSession(member, sessionId);

        return RspTemplate.success(Success.DELETE_WORKOUT_SUCCESS, Success.DELETE_WORKOUT_SUCCESS.getMessage());
    }
}
