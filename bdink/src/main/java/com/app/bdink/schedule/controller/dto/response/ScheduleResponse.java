package com.app.bdink.schedule.controller.dto.response;

import com.app.bdink.schedule.domain.CompetitionType;
import com.app.bdink.schedule.entity.Schedule;
import java.time.LocalDate;

public record ScheduleResponse(
    String title,
    LocalDate date,
    String scheduleType,
    String competionImage,
    String competitionType) {
    public ScheduleResponse(Schedule schedule) {
        this(schedule.getTitle(),
                schedule.getDate(),
                schedule.getScheduleType().name(),
                schedule.getCompetitionImage(),
                schedule.getCompetitionType().name());
    }
}
