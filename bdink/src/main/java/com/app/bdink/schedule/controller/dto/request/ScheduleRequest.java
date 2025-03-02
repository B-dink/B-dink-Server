package com.app.bdink.schedule.controller.dto.request;

import com.app.bdink.schedule.entity.Schedule;
import com.app.bdink.schedule.domain.ScheduleType;
import java.time.LocalDate;

public record ScheduleRequest(
    String title,
    LocalDate date,
    String scheduleType
) {
public Schedule toEntity() {
    return Schedule.builder()
        .title(title)
        .date(date)
        .scheduleType(ScheduleType.valueOf(this.scheduleType.toUpperCase()))
        .build();
    }
}
