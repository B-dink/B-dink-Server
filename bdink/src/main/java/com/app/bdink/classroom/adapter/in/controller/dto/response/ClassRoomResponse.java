package com.app.bdink.classroom.adapter.in.controller.dto.response;

import com.app.bdink.price.domain.PriceDetail;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
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
    public static ClassRoomResponse of(final ClassRoomEntity classRoomEntity, final ClassRoomSummeryDto classRoomSummeryDto) {
        return ClassRoomResponse.builder()
            .id(classRoomEntity.getId())
            .title(classRoomEntity.getTitle())
            .introduction(classRoomEntity.getIntroduction())
            .thumbnail(classRoomEntity.getThumbnail())
            .introLink(classRoomEntity.getIntroLink())
            .priceDetail(classRoomEntity.getPriceDetail())
            .classRoomSummeryDto(classRoomSummeryDto)
            .build();
    }
}
