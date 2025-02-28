package com.app.bdink.classroom.controller.dto.response;

import com.app.bdink.classroom.entity.ClassRoom;

public record BookmarkResponse(
    String classRoomTitle,
    String instructor
) {

    public static BookmarkResponse from(final ClassRoom classRoom) {
        return new BookmarkResponse(
            classRoom.getTitle(),
            // TODO: instructor 고민해보기
            classRoom.getInstructor().getMember().getName()
        );
    }
}
