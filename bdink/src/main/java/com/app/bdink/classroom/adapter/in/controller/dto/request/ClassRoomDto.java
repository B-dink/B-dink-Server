package com.app.bdink.classroom.adapter.in.controller.dto.request;

import com.app.bdink.classroom.domain.Career;
import com.app.bdink.classroom.domain.Level;
import com.app.bdink.price.controller.dto.PriceDto;

public record ClassRoomDto(
        String title,
        String introduction,
        String otLink,
        String subtitles,
        PriceDto priceDto,
        Level level,
        Career career
) {
}
