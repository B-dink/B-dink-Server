package com.app.bdink.schedule.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.schedule.domain.CompetitionType;
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

    private String competitionImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type", length = 10)
    private ScheduleType scheduleType;

    @Enumerated(EnumType.STRING)
    private CompetitionType competitionType;

    @Builder
    public Schedule(String title, LocalDate date, String competitionImage,ScheduleType scheduleType, CompetitionType competitionType) {
        this.title = title;
        this.date = date;
        this.competitionImage = competitionImage;
        this.scheduleType = scheduleType;
        this.competitionType = competitionType;
    }

    public void update(String title, LocalDate date,
                       String scheduleType,
                       String competitionImage,
                       CompetitionType competitionType) {
        this.title = updateTitle(title);
        this.date = updateDate(date);
        this.scheduleType = updateScheduleType(scheduleType);
        this.competitionImage = updateImage(competitionImage);
        this.competitionType = updateCompetitionType(competitionType);
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
    public String updateImage(String image) {
        if (image == null)
            return this.competitionImage;
        this.competitionImage = image;
        return this.competitionImage;
    }
    private CompetitionType updateCompetitionType(CompetitionType competitionType) {
        if (competitionType == null)
            return this.competitionType;
        return competitionType;
    }

}
