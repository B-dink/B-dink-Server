package com.app.bdink.classroom.controller.dto.response;

import com.app.bdink.classroom.entity.ClassRoom;

public record ClassRoomResponse(
        Long id,
        String title,
        String introduction
) {
    public static ClassRoomResponse from(final ClassRoom classRoom){
        return new ClassRoomResponse(
                classRoom.getId(),
                classRoom.getTitle(),
                classRoom.getIntroduction()
        );
    }
}
