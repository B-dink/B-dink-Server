package com.app.bdink.classroom.service.command;

import com.app.bdink.classroom.adapter.in.controller.dto.request.ClassRoomDto;
import com.app.bdink.classroom.adapter.out.persistence.entity.Instructor;

public record CreateClassRoomCommand(
        Instructor instructor,
        String thumbnailKey,
        String mediaKey,
        ClassRoomDto classRoomDto
)
{
    public static CreateClassRoomCommand of(Instructor instructor,
                                            String thumbnailKey,
                                            String mediaKey,
                                            ClassRoomDto classRoomDto){
        return new CreateClassRoomCommand(instructor,thumbnailKey,mediaKey,classRoomDto);
    }
}
