package com.app.bdink.classroom.adapter.in.controller.dto.request;

import com.app.bdink.classroom.domain.Level;

public record ClassRoomDto(
        String title,

        String introduction,
        PriceDto priceDto,
        Level level
) {
}
