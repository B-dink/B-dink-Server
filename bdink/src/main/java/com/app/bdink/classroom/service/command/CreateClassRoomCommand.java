package com.app.bdink.classroom.service.command;

import com.app.bdink.classroom.adapter.in.controller.dto.request.ClassRoomDto;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;

import java.util.List;

public record CreateClassRoomCommand(
        Instructor instructor,
        String thumbnailKey,
        List<String> detailImageKey,
        String mediaKey,
        ClassRoomDto classRoomDto
) {
    public static CreateClassRoomCommand of(Instructor instructor,
                                            String thumbnailKey,
                                            String mediaKey,
                                            List<String> detailImageKey,
                                            ClassRoomDto classRoomDto) {
        return new CreateClassRoomCommand(instructor, thumbnailKey, detailImageKey, mediaKey, classRoomDto);
    }
}
