package com.app.bdink.classroom.controller.dto.request;

import com.app.bdink.classroom.domain.ChapterSummary;
import com.app.bdink.classroom.entity.ClassRoom;

import java.time.LocalTime;

public record ClassRoomDto(
        String title,

        String introduction,

        PriceDto priceDto
) {
}
