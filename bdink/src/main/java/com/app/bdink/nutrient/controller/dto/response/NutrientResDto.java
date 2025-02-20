package com.app.bdink.nutrient.controller.dto.response;

import com.app.bdink.nutrient.entity.Nutrient;

public record NutrientResDto(
    String title,
    String content,
    String imgUrl,
    int likeCount,
    int commentCount
) {
    public NutrientResDto(Nutrient nutrient) {
        this(nutrient.getTitle(), nutrient.getContent(), nutrient.getImgUrl(), nutrient.getLikeCount(), nutrient.getCommentCount());
    }
}
