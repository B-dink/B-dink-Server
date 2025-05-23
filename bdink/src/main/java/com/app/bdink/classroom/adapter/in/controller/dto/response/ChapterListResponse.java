package com.app.bdink.classroom.adapter.in.controller.dto.response;

import java.time.LocalDate;
import java.util.List;

public record ChapterListResponse(
        double totalProgress,
        int totalLectures,
        int completedLectures,
        LocalDate expiredDate,
        List<ChapterResponse> chapters
) {
    public static ChapterListResponse of(double totalProgress, int totalLectures,
                                         int completedLectures, LocalDate expiredDate,
                                         List<ChapterResponse> chapters) {
        return new ChapterListResponse(totalProgress, totalLectures, completedLectures, expiredDate, chapters);
    }
}