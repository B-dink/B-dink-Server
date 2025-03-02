package com.app.bdink.classroom.controller.dto.request;

import com.app.bdink.classroom.domain.ChapterSummary;
import com.app.bdink.classroom.entity.ClassRoom;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;

public record ClassRoomDto(
        String title,

        String introduction,
        PriceDto priceDto
) {
}
