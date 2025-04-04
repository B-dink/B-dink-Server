package com.app.bdink.classroom.adapter.in.controller.dto.response;

import java.util.List;

public record CareerListDto(
        List<PromotionDto> promotionDtos,
        List<CategorizedClassroomDto> classroomDtoByCareer
) {
    public static CareerListDto of(List<PromotionDto> promotionList, List<CategorizedClassroomDto> careerList){
        return new CareerListDto(promotionList,careerList);
    }
}
