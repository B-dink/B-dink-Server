package com.app.bdink.review.service;

import com.app.bdink.member.repository.MemberRepository;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.review.adapter.in.controller.dto.request.ReviewRequest;
import com.app.bdink.review.adapter.in.controller.dto.response.ReviewResponse;
import com.app.bdink.review.domain.Review;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.review.repository.ReviewRepository;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
//    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(
            () -> new CustomException(Error.NOT_FOUND_REVIEW, Error.NOT_FOUND_REVIEW.getMessage())
        );
    }

    @Transactional
    public String saveReview(final Member member, final ClassRoomEntity classRoom, ReviewRequest reviewRequest) {
        if (reviewRepository.existsByClassRoomAndMember(classRoom, member)) {
            throw new CustomException(Error.EXIST_REVIEW, Error.EXIST_REVIEW.getMessage());
        }
        // TODO: 강의 다 수강했는지 확인
        Review review = Review.builder()
            .classRoom(classRoom)
            .member(member)
            .content(reviewRequest.content())
            .build();
        return String.valueOf(reviewRepository.save(review).getId());
    }

    public List<ReviewResponse> getAllReview(final ClassRoomEntity classRoomEntity, final Member member, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByClassRoom(classRoomEntity, pageable);
        return reviews.stream()
                .map(review -> {
                    boolean isAuthor = review.getMember().getId().equals(member.getId());
                    return ReviewResponse.from(review, isAuthor);
                })
                .toList();
    }

    @Transactional
    public void updateReview(Long reviewId, final Member member, ReviewRequest reviewRequest) {
        Review review = findById(reviewId);
        validateReview(review, member);
        review.update(reviewRequest.content());
    }

    @Transactional
    public void deleteReview(Long reviewId, final Member member) {
        Review review = findById(reviewId);
        validateReview(review, member);
        reviewRepository.deleteById(reviewId);
    }

    public int countReview(ClassRoomEntity classRoom) {
        return reviewRepository.countByClassRoom(classRoom);
    }

    public void validateReview(final Review review, final Member member) {
        if (!review.getMember().equals(member)) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }
    }
}
