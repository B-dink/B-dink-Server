package com.app.bdink.review.adapter.in.controller.dto.response;

import com.app.bdink.review.domain.Review;

public record ReviewResponse(
    String content,
    String memberName,
    String memberProfile
) {

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
            review.getContent(),
            review.getMember().getName(),
            review.getMember().getPictureUrl()
        );
    }
}
