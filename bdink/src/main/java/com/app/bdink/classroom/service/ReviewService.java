package com.app.bdink.classroom.service;

import com.app.bdink.classroom.controller.dto.request.ReviewRequest;
import com.app.bdink.classroom.controller.dto.response.ReviewResponse;
import com.app.bdink.classroom.domain.Review;
import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.repository.ReviewRepository;
import com.app.bdink.member.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(
            () -> new IllegalStateException("해당 리뷰를 찾지 못했습니다.")
        );
    }

    @Transactional
    public String saveReview(final Member member, final ClassRoom classRoom, ReviewRequest reviewRequest) {
        // TODO: 이미 리뷰를 만들었는지 & 강의 다 수강했는지 확인
        Review review = Review.builder()
            .classRoom(classRoom)
            .member(member)
            .content(reviewRequest.content())
            .build();
        return String.valueOf(reviewRepository.save(review).getId());
    }

    public List<ReviewResponse> getAllReview(final ClassRoom classRoom, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByClassRoom(classRoom, pageable);
        return reviews.stream()
            .map(ReviewResponse::from)
            .toList();
    }

    @Transactional
    public void updateReview(Long reviewId, final Member member, ReviewRequest reviewRequest) {
        // TODO: 로그인한 멤버와 리뷰를 작성한 멤버가 같은지 확인
        Review review = findById(reviewId);
        review.update(reviewRequest.content());
    }

    @Transactional
    public void deleteReview(Long reviewId, final Member member) {
        // TODO: 로그인한 멤버와 리뷰를 작성한 멤버가 같은지 확인
        reviewRepository.deleteById(reviewId);
    }

}
