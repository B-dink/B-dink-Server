package com.app.bdink.review.adapter.in.controller.dto.response;

import com.app.bdink.review.domain.Review;

public record ReviewResponse(
    String content,
    String memberName,
    String memberProfile,
    Long reviewId,
    boolean isAuthor
) {

    public static ReviewResponse from(Review review, boolean isAuthor) {
        return new ReviewResponse(
            review.getContent(),
            review.getMember().getName(),
            review.getMember().getPictureUrl(),
            review.getId(),
            isAuthor
        );
    }
}
