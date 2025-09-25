package com.app.bdink.classroom.adapter.in.controller.dto.response;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;

public record PromotionDto(
        Long classRoomId,

        String promotionThumbnail,
        String promotionLink
) {
    public static PromotionDto from(ClassRoomEntity classRoom){
        return new PromotionDto(
                classRoom.getId(),
                classRoom.getPromotionThumbnail(),
                classRoom.getIntroLink()
        );
    }
}
