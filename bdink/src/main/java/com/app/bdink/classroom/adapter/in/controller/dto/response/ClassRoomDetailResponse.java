package com.app.bdink.classroom.adapter.in.controller.dto.response;

import com.app.bdink.classroom.domain.Level;
import com.app.bdink.price.domain.PriceDetail;

public record ClassRoomDetailResponse(
        String title,
        String introduction,
        long bookmarkCount,
        String instructor,

        String instructorProfile,

        String thumbnail,
        PriceDetail priceDetail,
        Level level
) {
}
