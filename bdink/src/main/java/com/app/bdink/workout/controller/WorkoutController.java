package com.app.bdink.workout.controller;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.openai.dto.request.AiWorkoutMemoReqDto;
import com.app.bdink.workout.controller.dto.request.WorkoutFeedbackSaveReqDto;
import com.app.bdink.workout.controller.dto.request.WorkoutFeedbackUploadUrlReqDto;
import com.app.bdink.workout.controller.dto.request.WorkoutSessionSaveReqDto;
import com.app.bdink.workout.controller.dto.response.*;
import com.app.bdink.workout.service.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

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
                                             @RequestBody WorkoutSessionSaveReqDto requestDto) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        // workoutservice 계층에서 기록완료 메서드 사용
        return RspTemplate.success(Success.CREATE_EXERCISELIST_SUCCESS, workoutService.saveWorkoutSession(member, requestDto));
    }

    @DeleteMapping("/{sessionId}")
    @Operation(method = "DELETE", description = "운동일지를 삭제합니다.")
    public RspTemplate<?> deleteWorkoutSession(Principal principal, @PathVariable Long sessionId) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        workoutService.deleteWorkoutSession(member, sessionId);

        return RspTemplate.success(Success.DELETE_WORKOUT_SUCCESS, Success.DELETE_WORKOUT_SUCCESS.getMessage());
    }

    @PutMapping("/{sessionId}")
    @Operation(method = "PUT", description = "운동일지를 수정합니다.")
    public RspTemplate<?> updateWorkoutSession(Principal principal,
                                               @PathVariable Long sessionId,
                                               @RequestBody WorkoutSessionSaveReqDto requestDto) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        String updateId = workoutService.updateWorkoutSession(member, sessionId, requestDto);

        return RspTemplate.success(Success.UPDATE_EXERCISELIST_SUCCESS, updateId);
    }

    @GetMapping("/calendar")
    @Operation(method = "GET", description = "특정 월에 운동한 날짜들을 조회합니다.")
    public RspTemplate<?> getWorkoutCalendar(Principal principal,
                                             @RequestParam int year,
                                             @RequestParam int month) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));

        return RspTemplate.success(Success.GET_WORKOUT_CALENDAR_SUCCESS, workoutService.getWorkoutCalender(member, year, month));
    }

    @GetMapping("/volumeStatus")
    @Operation(method = "GET", description = "볼륨 현황(주간 랭킹/ 주간 볼륨/오늘 볼륨)을 조회합니다.")
    public RspTemplate<?> getVolumeStatus(Principal principal) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));

        LocalDate base = LocalDate.now();

        VolumeStatusResDto dto = workoutService.getVolumeStatus(member, base);

        return RspTemplate.success(Success.GET_VOLUME_STATUS_SUCCESS, dto);
    }

    @GetMapping("/volumeGraph")
    @Operation(method = "GET", description = "지난주, 이번주 일별 볼륨 데이터를 조회합니다.")
    public RspTemplate<?> getWeeklyVolumeGraph(Principal principal) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));

        LocalDate base = LocalDate.now();

        WeeklyVolumeGraphResDto dto = workoutService.getWeeklyVolumeGraph(member, base);

        return RspTemplate.success(Success.GET_VOLUME_GRAPH_SUCCESS, dto);
    }

    @GetMapping("/day")
    @Operation(method = "GET", description = "선택한 날짜의 운동일지 상세 정보를 조회합니다.")
    public RspTemplate<?> getWorkoutDay(Principal principal,
                                        @RequestParam
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));

        WorkoutDailyDetailResDto dto = workoutService.getWorkoutDailyDetail(member, date);

        return RspTemplate.success(Success.GET_WORKOUT_SESSION_DETAIL_SUCCESS, dto);

    }

    @GetMapping("/{sessionId}")
    @Operation(method = "GET", description = "운동일지 상세 정보를 세션 ID로 조회합니다.")
    public RspTemplate<?> getWorkoutSessionById(Principal principal,
                                                @PathVariable Long sessionId) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));

        WorkoutDailyDetailResDto dto = workoutService.getWorkoutDailyDetailBySession(member, sessionId);

        return RspTemplate.success(Success.GET_WORKOUT_SESSION_DETAIL_SUCCESS, dto);

    }

    @GetMapping("/{sessionId}/feedback")
    @Operation(method = "GET", description = "운동일지 피드백을 세션 ID로 조회합니다.")
    public RspTemplate<?> getWorkoutSessionFeedback(Principal principal,
                                                    @PathVariable Long sessionId) {
        Long memberId = memberUtilService.getMemberId(principal);
        WorkoutFeedbackResDto dto = workoutService.getWorkoutFeedbackBySession(memberId, sessionId);
        return RspTemplate.success(Success.GET_WORKOUT_SESSION_DETAIL_SUCCESS, dto);
    }

    @PostMapping("/{sessionId}/feedback")
    @Operation(method = "POST", description = "트레이너 피드백을 등록합니다.")
    public RspTemplate<?> createWorkoutFeedback(Principal principal,
                                                @PathVariable Long sessionId,
                                                @RequestBody WorkoutFeedbackSaveReqDto requestDto) {
        Long trainerId = memberUtilService.getMemberId(principal);
        WorkoutFeedbackResDto dto = workoutService.saveWorkoutFeedback(trainerId, sessionId, requestDto);
        return RspTemplate.success(Success.UPDATE_EXERCISELIST_SUCCESS, dto);
    }

    @PostMapping("/feedback/upload-urls")
    @Operation(method = "POST", description = "피드백 이미지/영상 업로드용 presigned URL을 발급합니다.")
    public RspTemplate<?> getWorkoutFeedbackUploadUrls(@RequestBody WorkoutFeedbackUploadUrlReqDto requestDto) {
        WorkoutFeedbackUploadUrlsResDto dto = workoutService.getWorkoutFeedbackUploadUrls(requestDto);
        return RspTemplate.success(Success.CREATE_PRESIGNURL_SUCCESS, dto);
    }

    @GetMapping("/volume/weeklyCompare")
    @Operation(method = "GET", description = "지난주 대비 운동량 및 운동횟수를 조회합니다.")
    public RspTemplate<?> getWorkoutVolume(Principal principal) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));

        LocalDate base = LocalDate.now();

        WeeklyVolumeCompareResDto dto = workoutService.getWeeklyVolumeCompare(member, base);

        return RspTemplate.success(Success.GET_WEEKLY_COMPARE_VOLUME_SUCCESS, dto);
    }

    @PostMapping("/ai-memo")
    @Operation(method = "POST", description = "AI 메모로 운동일지 정보 DTO를 생성합니다.")
    public RspTemplate<?> createWorkoutSessionByAi(@RequestBody AiWorkoutMemoReqDto reqDto){

        AiMemoResDto dto = workoutService.convertMemoTextToRequestDto(reqDto);

        return RspTemplate.success(Success.CREATE_AI_MEMO_SUCCESS, dto);
    }
}
