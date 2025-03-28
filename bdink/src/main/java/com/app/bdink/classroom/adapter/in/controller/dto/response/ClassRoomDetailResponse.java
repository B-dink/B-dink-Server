package com.app.bdink.classroom.adapter.in.controller.dto.response;

import com.app.bdink.classroom.domain.Level;
import com.app.bdink.classroom.domain.PriceDetail;

public record ClassRoomDetailResponse(
        String title,
        String introduction,
        long bookmarkCount,
        String instructor,

        String thumbnail,
        PriceDetail priceDetail,
        Level level
) {
}
