package com.app.bdink.classroom.adapter.in.controller.dto.response;

import com.app.bdink.chapter.domain.ChapterSummary;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.member.entity.Member;
import com.app.bdink.price.domain.PriceDetail;
import java.util.Optional;

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
        String instructorName = Optional.ofNullable(classRoomEntity.getInstructor())
                .map(Instructor::getMember)
                .map(Member::getName)
                .orElse("강사 정보 없음");

        return new AllCareerClassRoomResponse(
                classRoomEntity.getId(),
                classRoomEntity.getCareer(),
                classRoomEntity.getTitle(),
                classRoomEntity.getThumbnail(),
                instructorName,
                classRoomEntity.getPriceDetail(),
                isBookmarked,
                chapterSummary.getTotalLectureCount(),
                totalReviewCount
        );
    }
}
