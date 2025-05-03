package com.app.bdink.classroom.adapter.in.controller.dto.response;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomDetailImage;

public record ClassRoomImageResponse(
        Long id,
        String imageUrl,
        int sortOrder
) {

    public static ClassRoomImageResponse from(ClassRoomDetailImage entity) {
        return new ClassRoomImageResponse(
                entity.getId(),
                entity.getImageUrl(),
                entity.getSortOrder()
        );
    }
}

