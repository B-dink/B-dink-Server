package com.app.bdink.classroom.service.command;

import com.app.bdink.classroom.adapter.in.controller.dto.request.ClassRoomDto;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;

import java.util.List;

public record CreateClassRoomCommand(
        Instructor instructor,
        //썸네일 이미지들에 관련된 파라미터
        String thumbnailKey,
        String promotionThumbnailKey,
        String detailThumbnailKey,
        //디테일 페이지에 필요한 파라미터
        List<String> detailImageKey,
        String mediaKey,
        ClassRoomDto classRoomDto
) {
    public static CreateClassRoomCommand of(Instructor instructor,
                                            String thumbnailKey,
                                            String promotionThumbnailKey,
                                            String detailThumbnailKey,
                                            String mediaKey,
                                            List<String> detailImageKey,
                                            ClassRoomDto classRoomDto) {
        return new CreateClassRoomCommand(instructor, thumbnailKey, promotionThumbnailKey, detailThumbnailKey, detailImageKey, mediaKey, classRoomDto);
    }
}
