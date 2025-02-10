package com.app.bdink.classroom.controller.dto.request;

import com.app.bdink.classroom.entity.ClassRoom;

public record ClassRoomDto(
        String title,
        String introduction
) {
    public ClassRoom toEntity(){
        return ClassRoom.builder()
                .title(title)
                .introduction(introduction)
                .build();
    }
}
