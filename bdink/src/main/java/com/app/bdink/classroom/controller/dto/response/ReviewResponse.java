package com.app.bdink.classroom.controller.dto.response;

import com.app.bdink.classroom.domain.Review;

public record ReviewResponse(
    String content,
    String memberName
) {

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
            review.getContent(),
            review.getMember().getName()
        );
    }
}
