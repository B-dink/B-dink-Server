package com.app.bdink.bookmark.adapter.in.controller.dto.response;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;

public record BookmarkResponse(
    String classRoomTitle,
    String instructor,
    Long classRoomId
) {

    public static BookmarkResponse from(final ClassRoomEntity classRoomEntity) {
        return new BookmarkResponse(
            classRoomEntity.getTitle(),
            // TODO: instructor 고민해보기
            classRoomEntity.getInstructor().getMember().getName(),
            classRoomEntity.getId()
        );
    }
}
