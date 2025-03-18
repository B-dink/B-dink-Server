package com.app.bdink.schedule.controller;

import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.schedule.controller.dto.request.ScheduleRequest;
import com.app.bdink.schedule.service.ScheduleService;
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
@RequestMapping("/api/v1/schedule")
@Tag(name = "캘린더 API", description = "대회, 재활, 영양 캘린더와 관련된 API들입니다.")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(method = "POST", description = "캘린더 일정을 생성합니다.")
    @PostMapping
    public RspTemplate<?> createSchedule(@RequestBody ScheduleRequest scheduleRequest) {
        return RspTemplate.success(Success.CREATE_SCHEDULE_SUCCESS, CreateIdDto.from(scheduleService.createSchedule(scheduleRequest)));
    }

    @Operation(method = "GET", description = "모든 캘린더 일정을 조회합니다.")
    @GetMapping
    public RspTemplate<?> getAllSchedule() {
        return RspTemplate.success(Success.GET_ALL_SCHEDULE_SUCCESS, scheduleService.getAllSchedule());
    }

    @Operation(method = "GET", description = "카테고리 별 캘린더 일정을 조회합니다.")
    @GetMapping("/type")
    public RspTemplate<?> getScheduleByCategory(@RequestParam String scheduleType) {
        return RspTemplate.success(Success.GET_SCHEDULE_BY_CATEGORY_SUCCESS, scheduleService.getScheduleByScheduleType(scheduleType.toUpperCase()));
    }

    @Operation(method = "PUT", description = "캘린더 일정을 수정합니다.")
    @PutMapping
    public RspTemplate<?> updateSchedule(@RequestParam Long id, @RequestBody ScheduleRequest scheduleRequest) {
        scheduleService.updateSchedule(id, scheduleRequest);
        return RspTemplate.success(Success.UPDATE_SCHEDULE_SUCCESS, Success.UPDATE_SCHEDULE_SUCCESS.getMessage());
    }

    @Operation(method = "DELETE", description = "캘린더 일정을 삭제합니다.")
    @DeleteMapping
    public RspTemplate<?> deleteSchedule(@RequestParam Long id) {
        scheduleService.deleteSchedule(id);
        return RspTemplate.success(Success.DELETE_SCHEDULE_SUCCESS, Success.DELETE_SCHEDULE_SUCCESS.getMessage());
    }
}
