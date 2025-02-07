package com.app.bdink.classroom.domain;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
public class ChapterSummary {

    private int totalChapterCount; // 총 챕터 개수

    private LocalDateTime totalLectureTime; // 총 강좌 시간

    public ChapterSummary(int totalChapterCount, final LocalDateTime totalLectureTime) {
        this.totalChapterCount = totalChapterCount;
        this.totalLectureTime = totalLectureTime;
    }
}
