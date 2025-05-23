package com.app.bdink.classroom.adapter.in.controller.dto.response;

import com.app.bdink.chapter.entity.Chapter;
import com.app.bdink.external.kollus.entity.KollusMediaLink;
import com.app.bdink.lecture.controller.dto.response.LectureResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record ChapterResponse(
    String title,
    List<LectureResponse> lectures
) {
    public static ChapterResponse of(final Chapter chapter,
                                     Map<Long, KollusMediaLink> mediaLinkMap) {

        List<LectureResponse> lectureResponses = chapter.getLectures().stream()
                .map(lecture -> {
                    Integer percent = Optional.ofNullable(mediaLinkMap.get(lecture.getId()))
                            .map(KollusMediaLink::getPlaytimePercent)
                            .orElse(0);
                    return LectureResponse.from(lecture, percent);
                })
                .toList();

        return new ChapterResponse(chapter.getTitle(), lectureResponses);
    }
}
