package com.app.bdink.classroom.adapter.in.controller.dto.response;

import com.app.bdink.classroom.domain.Level;
import com.app.bdink.price.domain.PriceDetail;

import java.util.List;

public record ClassRoomDetailResponse(
        String title,
        String introduction,
        long bookmarkCount,
        String instructor,

        String instructorProfile,

        String thumbnail,

        Boolean payment,

        PriceDetail priceDetail,
        Level level,
        Boolean isBookmarked,

        List<String> detailPageImageUrls
) {
}
