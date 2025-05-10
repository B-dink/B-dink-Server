package com.app.bdink.bookmark.adapter.in.controller;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.bookmark.service.BookmarkService;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public RspTemplate<CreateIdDto> saveBookmark(Principal principal, @RequestParam Long classRoomId) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        ClassRoomEntity classRoomEntity = classRoomService.findById(classRoomId);
        String id = bookmarkService.saveBookmark(member, classRoomEntity);
        return RspTemplate.success(Success.CREATE_BOOKMARK_SUCCESS, CreateIdDto.from(id));
    }

    @GetMapping
    @Operation(method = "GET", description = "북마크한 클래스룸을 조회합니다.")
    public RspTemplate<?> getBookmarkClassRoom(Principal principal) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        return RspTemplate.success(Success.GET_BOOKMARK_SUCCESS,bookmarkService.getBookmarkClassRoom(member));
    }

    @DeleteMapping("/{bookmarkId}")
    @Operation(method = "DELETE", description = "북마크를 북마크 ID로 삭제합니다.")
    public RspTemplate<?> deleteBookmark(Principal principal, @PathVariable Long bookmarkId) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        bookmarkService.deleteBookmark(member, bookmarkId);
        return RspTemplate.success(Success.DELETE_BOOKMARK_SUCCESS,Success.DELETE_BOOKMARK_SUCCESS.getMessage());
    }

    @DeleteMapping("/classroom/{classRoomId}")
    @Operation(method = "DELETE", description = "북마크를 클래스 ID로 삭제합니다.")
    public RspTemplate<?> deleteBookmarkByClassRoomId(Principal principal, @PathVariable Long classRoomId) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        ClassRoomEntity classRoomEntity = classRoomService.findById(classRoomId);
        bookmarkService.deleteBookmarkByClassRoomId(member, classRoomEntity);
        return RspTemplate.success(Success.DELETE_BOOKMARK_SUCCESS,Success.DELETE_BOOKMARK_SUCCESS.getMessage());
    }
}
