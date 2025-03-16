package com.app.bdink.classroom.controller.dto.response;

import com.app.bdink.classroom.domain.PriceDetail;

public record ClassRoomDetailResponse(
        String title,
        String introduction,
        long bookmarkCount,
        String instructor,

        String thumbnail,
        PriceDetail priceDetail
) {
}
