package com.app.bdink.schedule.controller.dto.response;

import com.app.bdink.schedule.entity.Schedule;
import java.time.LocalDate;

public record ScheduleResponse(
    String title,
    LocalDate date,
    String scheduleType
) {
    public ScheduleResponse(Schedule schedule) {
        this(schedule.getTitle(), schedule.getDate(), schedule.getScheduleType().name());
    }
}
