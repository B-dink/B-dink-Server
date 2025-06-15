package com.app.bdink.instructor.adapter.in.controller.dto.response;

import com.app.bdink.chapter.domain.ChapterSummary;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.price.domain.PriceDetail;

public record InstructorClassroomDto(
        Long id,
        Career career,
        String title,
        String classRoomThumbnail,
        String instructor,
        PriceDetail priceDetail,
        String marketingImage,
        String marketingText,
        String marketingSites,

        int totalLectureCount,
        int totalReviewCount
) {
    public static InstructorClassroomDto of(
            final ClassRoomEntity classRoomEntity, final Instructor instructor, final ChapterSummary chapterSummary, int totalReviewCount){
        return new InstructorClassroomDto(
                classRoomEntity.getId(),
                classRoomEntity.getCareer(),
                classRoomEntity.getTitle(),
                classRoomEntity.getThumbnail(),
                classRoomEntity.getInstructor().getMember().getName(),
                classRoomEntity.getPriceDetail(),
                instructor.getMarketingImage(),
                instructor.getMarketingText(),
                instructor.getMarketingSites(),
                chapterSummary.getTotalLectureCount(),
                totalReviewCount
        );
    }
}
