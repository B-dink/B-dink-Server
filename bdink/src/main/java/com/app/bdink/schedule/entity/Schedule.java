package com.app.bdink.schedule.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.schedule.domain.ScheduleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type", length = 10)
    private ScheduleType scheduleType;

    @Builder
    public Schedule(String title, LocalDate date, ScheduleType scheduleType) {
        this.title = title;
        this.date = date;
        this.scheduleType = scheduleType;
    }

    public void update(String title, LocalDate date, String scheduleType) {
        this.title = updateTitle(title);
        this.date = updateDate(date);
        this.scheduleType = updateScheduleType(scheduleType);
    }

    public String updateTitle(String title) {
        if (title.isBlank() || title == null)
            return this.title;
        return title;
    }

    private LocalDate updateDate(LocalDate date) {
        if (date == null)
            return this.date;
        return date;
    }

    private ScheduleType updateScheduleType(String scheduleType) {
        if (scheduleType == null)
            return this.scheduleType;
        return ScheduleType.valueOf(scheduleType);
    }

}
