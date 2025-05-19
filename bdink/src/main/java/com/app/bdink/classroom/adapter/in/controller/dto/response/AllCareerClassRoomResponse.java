package com.app.bdink.classroom.adapter.in.controller.dto.response;

import com.app.bdink.chapter.domain.ChapterSummary;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.price.domain.PriceDetail;

public record AllCareerClassRoomResponse(
        Long id,
        Career career,
        String title,
        String classRoomThumbnail,
        String instructor,
        PriceDetail priceDetail,
        Boolean isBookmarked,

        int totalLectureCount,
        int totalReviewCount
) {
    public static AllCareerClassRoomResponse of(final ClassRoomEntity classRoomEntity, final ChapterSummary chapterSummary, Boolean isBookmarked, int totalReviewCount){
        return new AllCareerClassRoomResponse(
                classRoomEntity.getId(),
                classRoomEntity.getCareer(),
                classRoomEntity.getTitle(),
                classRoomEntity.getThumbnail(),
                classRoomEntity.getInstructor().getMember().getName(),
                classRoomEntity.getPriceDetail(),
                isBookmarked,
                chapterSummary.getTotalLectureCount(),
                totalReviewCount
        );
    }
}
