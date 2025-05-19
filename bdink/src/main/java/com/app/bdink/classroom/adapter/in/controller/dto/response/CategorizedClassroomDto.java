package com.app.bdink.classroom.adapter.in.controller.dto.response;

import java.util.List;

public record CategorizedClassroomDto(
        List<AllCareerClassRoomResponse> classrooms
) {
    public static CategorizedClassroomDto from(List<AllCareerClassRoomResponse> careerClassroomDtos){
        return new CategorizedClassroomDto(careerClassroomDtos);
    }
}
