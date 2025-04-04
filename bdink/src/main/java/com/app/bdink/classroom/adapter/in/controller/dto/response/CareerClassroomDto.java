package com.app.bdink.classroom.adapter.in.controller.dto.response;

import com.app.bdink.chapter.domain.ChapterSummary;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.price.controller.dto.PriceDto;
import com.app.bdink.price.domain.PriceDetail;

public record CareerClassroomDto(
        Long id,
        Career career,
        String title,
        String instructor,
        PriceDetail priceDetail,

        int totalLectureCount,
        int totalReviewCount
) {
    public static CareerClassroomDto of(final ClassRoomEntity classRoomEntity, final ChapterSummary chapterSummary, int totalReviewCount){
        return new CareerClassroomDto(
                classRoomEntity.getId(),
                classRoomEntity.getCareer(),
                classRoomEntity.getTitle(),
                classRoomEntity.getInstructor().getMember().getName(),
                classRoomEntity.getPriceDetail(),
                chapterSummary.getTotalLectureCount(),
                totalReviewCount
        );
    }
}
