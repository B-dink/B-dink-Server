package com.app.bdink.classroom.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
//TODO: dto로 빼는게 나아보임.
public class ChapterSummary {

    private int totalChapterCount; // 총 챕터 개수

    private int totalLectureCount; // 총 강좌 개수

    private LocalTime totalLectureTime; // 총 강좌 시간

    public ChapterSummary(int totalChapterCount, int totalLectureCount ,final LocalTime totalLectureTime) {
        this.totalChapterCount = totalChapterCount;
        this.totalLectureCount = totalLectureCount;
        this.totalLectureTime = totalLectureTime;
    }


    public void updateTotalChapterCount(int totalChapterCount) {
        this.totalChapterCount = totalChapterCount;
    }

    public ChapterSummary updateTotalLectureTime(final LocalTime lectureTime) {
        LocalTime calculatedTime = this.totalLectureTime
                .plusHours(lectureTime.getHour())
                .plusMinutes(lectureTime.getMinute())
                .plusSeconds(lectureTime.getSecond());

        return new ChapterSummary(this.totalChapterCount, this.totalLectureCount, calculatedTime);

    }
}
