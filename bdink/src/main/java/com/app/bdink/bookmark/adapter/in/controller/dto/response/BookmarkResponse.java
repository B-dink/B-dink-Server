package com.app.bdink.bookmark.adapter.in.controller.dto.response;

import com.app.bdink.bookmark.entity.Bookmark;

public record BookmarkResponse(
    String classRoomTitle,
    String instructor,
    Long classRoomId,
    String thumbnail,
    Integer totalLectureCount
) {

    public static BookmarkResponse from(final Bookmark bookmark, final Integer totalLectureCount) {
        return new BookmarkResponse(
                bookmark.getClassRoom().getTitle(),
                bookmark.getClassRoom().getInstructor().getMember().getName(),
                bookmark.getClassRoom().getId(),
                bookmark.getClassRoom().getThumbnail(),
                totalLectureCount
        );
    }
}
