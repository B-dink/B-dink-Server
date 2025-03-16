package com.app.bdink.classroom.controller.dto.response;

import com.app.bdink.classroom.domain.PriceDetail;
import com.app.bdink.classroom.entity.ClassRoom;
import lombok.Builder;

@Builder
public record ClassRoomResponse(
        Long id,
        String title,
        String introduction,
        String thumbnail,

        String introLink,

        PriceDetail priceDetail,
        ClassRoomSummeryDto classRoomSummeryDto // 수정 X
) {
    public static ClassRoomResponse of(final ClassRoom classRoom, final ClassRoomSummeryDto classRoomSummeryDto) {
        return ClassRoomResponse.builder()
            .id(classRoom.getId())
            .title(classRoom.getTitle())
            .introduction(classRoom.getIntroduction())
            .thumbnail(classRoom.getThumbnail())
            .introLink(classRoom.getIntroLink())
            .priceDetail(classRoom.getPriceDetail())
            .classRoomSummeryDto(classRoomSummeryDto)
            .build();
    }
}
