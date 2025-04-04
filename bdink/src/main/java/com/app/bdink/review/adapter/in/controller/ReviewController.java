package com.app.bdink.review.adapter.in.controller;

import com.app.bdink.review.adapter.in.controller.dto.request.ReviewRequest;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.review.service.ReviewService;
import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reviews")
@Tag(name = "리뷰 API", description = "클래스룸 리뷰와 관련된 API들입니다.")
public class ReviewController {

    private final ReviewService reviewService;
    private final MemberService memberService;
    private final ClassRoomService classRoomService;

    @Operation(method = "POST", description = "리뷰를 등록합니다.")
    @PostMapping
    public RspTemplate<?> saveReview(Principal principal, @RequestParam Long classRoomId,
                                        @RequestBody ReviewRequest reviewRequest) {
        Member member = memberService.findById(Long.parseLong(principal.getName()));
        ClassRoomEntity classRoomEntity = classRoomService.findById(classRoomId);
        String id = reviewService.saveReview(member, classRoomEntity, reviewRequest);
        return RspTemplate.success(Success.CREATE_REVIEW_SUCCESS, CreateIdDto.from(id));

    }

    @Operation(method = "GET", description = "모든 리뷰를 조회합니다.")
    @GetMapping
    public RspTemplate<?> getAllReview(@RequestParam Long classRoomId, @PageableDefault(size = 8) Pageable pageable) {
        ClassRoomEntity classRoomEntity = classRoomService.findById(classRoomId);
        return RspTemplate.success(Success.GET_REVIEW_SUCCESS, reviewService.getAllReview(classRoomEntity, pageable));
    }

    @Operation(method = "PUT", description = "리뷰를 수정합니다.")
    @PutMapping
    public RspTemplate<?> updateReview(@RequestParam Long reviewId, Principal principal,
                                          @RequestBody ReviewRequest reviewRequest) {
        Member member = memberService.findById(Long.parseLong(principal.getName()));
        reviewService.updateReview(reviewId, member, reviewRequest);
        return RspTemplate.success(Success.UPDATE_REVIEW_SUCCESS, Success.UPDATE_REVIEW_SUCCESS.getMessage());
    }

    @Operation(method = "DELETE", description = "리뷰를 삭제합니다.")
    @DeleteMapping
    public RspTemplate<?> deleteReview(@RequestParam Long reviewId, Principal principal) {
        Member member = memberService.findById(Long.parseLong(principal.getName()));
        reviewService.deleteReview(reviewId, member);
        return RspTemplate.success(Success.DELETE_REVIEW_SUCCESS,Success.DELETE_REVIEW_SUCCESS.getMessage());
    }

    @Operation(method = "GET", description = "클래스룸의 리뷰 개수를 조회합니다.")
    @GetMapping("/count")
    public RspTemplate<?> countReview(@RequestParam Long classRoomId) {
        ClassRoomEntity classRoomEntity = classRoomService.findById(classRoomId);
        return RspTemplate.success(Success.GET_REVIEW_COUNT_SUCCESS, reviewService.countReview(classRoomEntity));
    }
}
