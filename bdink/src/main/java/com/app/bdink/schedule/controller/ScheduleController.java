package com.app.bdink.schedule.controller;

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
@Tag(name = "Schedule API", description = "일정과 관련된 API들입니다.")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(method = "POST", description = "일정을 생성합니다.")
    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody ScheduleRequest scheduleRequest) {
        String id = scheduleService.createSchedule(scheduleRequest);
        return ResponseEntity.created(
            URI.create(id))
            .build();
    }

    @Operation(method = "GET", description = "모든 일정을 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getAllSchedule() {
        return ResponseEntity.ok().body(scheduleService.getAllSchedule());
    }

    @Operation(method = "GET", description = "카테고리 별 일정을 조회합니다.")
    @GetMapping("/type")
    public ResponseEntity<?> getScheduleByCategory(@RequestParam String scheduleType) {
        return ResponseEntity.ok().body(scheduleService.getScheduleByScheduleType(scheduleType.toUpperCase()));
    }

    @Operation(method = "PUT", description = "일정을 수정합니다.")
    @PutMapping
    public ResponseEntity<?> updateSchedule(@RequestParam Long id, @RequestBody ScheduleRequest scheduleRequest) {
        scheduleService.updateSchedule(id, scheduleRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(method = "DELETE", description = "일정을 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<?> deleteSchedule(@RequestParam Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok().build();
    }
}
