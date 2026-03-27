package com.app.bdink.classroom.adapter.in.controller.dto.response;

import com.app.bdink.chapter.entity.Chapter;
import com.app.bdink.learning.entity.LearningProgress;
import com.app.bdink.lecture.controller.dto.response.LectureResponse;

import java.util.List;
import java.util.Map;

public record ChapterResponse(
    String title,
    List<LectureResponse> lectures
) {
    public static ChapterResponse of(final Chapter chapter,
                                     Map<Long, LearningProgress> progressMap) {

        List<LectureResponse> lectureResponses = chapter.getLectures().stream()
                .map(lecture -> {
                    // 챕터 응답에서는 강의별 최종 진행률만 읽어서 표시용 DTO로 변환한다.
                    LearningProgress progress = progressMap.get(lecture.getId());
                    double percent = progress != null ? progress.getProgressPercent() : 0.0;
                    return LectureResponse.from(lecture, percent);
                })
                .toList();

        return new ChapterResponse(chapter.getTitle(), lectureResponses);
    }
}
