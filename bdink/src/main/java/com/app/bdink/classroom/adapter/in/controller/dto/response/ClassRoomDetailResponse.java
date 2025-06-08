package com.app.bdink.classroom.adapter.in.controller.dto.response;

import com.app.bdink.classroom.domain.Level;
import com.app.bdink.price.domain.PriceDetail;

import java.time.LocalDate;
import java.util.List;

public record ClassRoomDetailResponse(
        String title,
        String introduction,
        long bookmarkCount,
        String instructor,
        String instructorProfile,

        Integer totalChapterCount,
        Integer totalLectureCount,
        LocalDate expiredDate,
        String totalLectureTime,
        String subtitles,

        String thumbnail,

        boolean payment,

        PriceDetail priceDetail,
        Level level,
        Boolean isBookmarked,
        Long bookmarkId,

        List<String> detailPageImageUrls,

        List<ClassRoomDetailChapterResponse> chapters
) {
}
