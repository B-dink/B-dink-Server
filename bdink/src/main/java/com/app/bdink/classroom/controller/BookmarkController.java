package com.app.bdink.classroom.controller;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.service.BookmarkService;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.common.util.MemberUtilService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.security.Principal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/bookmark")
@Tag(name = "북마크 API", description = "클래스룸 북마크와 관련된 API들입니다.")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final ClassRoomService classRoomService;
    private final MemberService memberService;
    private final MemberUtilService memberUtilService;

    @PostMapping
    @Operation(method = "POST", description = "북마크를 저장합니다.")
    public ResponseEntity<?> saveBookmark(Principal principal, @RequestParam Long classRoomId) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        ClassRoom classRoom = classRoomService.findById(classRoomId);
        String id = bookmarkService.saveBookmark(member, classRoom);
        return ResponseEntity.created(
            URI.create(id))
            .build();
    }

    @GetMapping
    @Operation(method = "GET", description = "북마크한 클래스룸을 조회합니다.")
    public ResponseEntity<?> getBookmarkClassRoom(Principal principal) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        return ResponseEntity.ok().body(bookmarkService.getBookmarkClassRoom(member));
    }

    @DeleteMapping
    @Operation(method = "DELETE", description = "북마크를 삭제합니다.")
    public ResponseEntity<?> deleteBookmark(Principal principal, @RequestParam Long reviewId) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        bookmarkService.deleteBookmark(member, reviewId);
        return ResponseEntity.noContent().build();
    }
}
