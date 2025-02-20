package com.app.bdink.nutrient.controller.dto.request;

import com.app.bdink.nutrient.entity.Nutrient;

public record NutrientReqDto(
    String title,
    String content,
    String imgUrl
) {
    public Nutrient toEntity() {
        return Nutrient.builder()
            .title(title)
            .content(content)
            .imgUrl(imgUrl)
            .build();
    }
}
