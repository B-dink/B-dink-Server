package com.app.bdink.classroom.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor
@Getter
public class ChapterSummary {

    private int totalChapterCount; // 총 챕터 개수

    private int totalLectureCount; // 총 강좌 개수

    private LocalTime totalLectureTime; // 총 강좌 시간

    public ChapterSummary(int totalChapterCount, int totalLectureCount ,final int totalLectureTime) {
        this.totalChapterCount = totalChapterCount;
        this.totalLectureCount = totalLectureCount;
        this.totalLectureTime = LocalTime.of(totalLectureTime/60,totalLectureTime%60);
    }

}
