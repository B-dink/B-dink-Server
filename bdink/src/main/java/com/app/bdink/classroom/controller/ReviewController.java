package com.app.bdink.classroom.controller;

import com.app.bdink.classroom.controller.dto.request.ReviewRequest;
import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.classroom.service.ReviewService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "리뷰 API", description = "클래스룲 리뷰와 관련된 API들입니다.")
public class ReviewController {

    private final ReviewService reviewService;
    private final MemberService memberService;
    private final ClassRoomService classRoomService;

    @Operation(method = "POST", description = "리뷰를 등록합니다.")
    @PostMapping
    public ResponseEntity<?> saveReview(@RequestParam Long memberId, @RequestParam Long classRoomId,
        @RequestBody ReviewRequest reviewRequest) {
        Member member = memberService.findById(memberId);
        ClassRoom classRoom = classRoomService.findById(classRoomId);
        String id = reviewService.saveReview(member, classRoom, reviewRequest);
        return ResponseEntity.created(
            URI.create(id))
            .build();
    }

    @Operation(method = "GET", description = "모든 리뷰를 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getAllReview(@RequestParam Long classRoomId) {
        ClassRoom classRoom = classRoomService.findById(classRoomId);
        return ResponseEntity.ok().body(reviewService.getAllReview(classRoom));

    }

    @Operation(method = "PUT", description = "리뷰를 수정합니다.")
    @PutMapping
    public ResponseEntity<?> updateReview(@RequestParam Long reviewId, @RequestParam Long memberId, @RequestBody ReviewRequest reviewRequest) {
        Member member = memberService.findById(memberId);
        reviewService.updateReview(reviewId, member, reviewRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(method = "DELETE", description = "리뷰를 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<?> deleteReview(@RequestParam Long reviewId, @RequestParam Long memberId) {
        Member member = memberService.findById(memberId);
        reviewService.deleteReview(reviewId, member);
        return ResponseEntity.noContent().build();
    }
}
