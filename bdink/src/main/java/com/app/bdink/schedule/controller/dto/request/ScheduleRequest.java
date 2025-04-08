package com.app.bdink.schedule.controller.dto.request;

import com.app.bdink.schedule.domain.CompetitionType;
import com.app.bdink.schedule.entity.Schedule;
import com.app.bdink.schedule.domain.ScheduleType;
import java.time.LocalDate;

public record ScheduleRequest(
    String title,
    LocalDate date,
    String scheduleType,
    String competitionType
) {
public Schedule toEntity() {
    return Schedule.builder()
        .title(title)
        .date(date)
        .scheduleType(ScheduleType.valueOf(this.scheduleType.toUpperCase()))
        .competitionType(CompetitionType.valueOf(this.competitionType.toUpperCase()))
        .build();
    }
}
