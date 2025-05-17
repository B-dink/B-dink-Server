package com.app.bdink.mypage.controller;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.mypage.dto.response.MyPageResponse;
import com.app.bdink.mypage.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
@Tag(name = "MyPage API", description = "MyPage 관련 API들입니다.")
public class MyPageController {

    private final MemberService memberService;
    private final MyPageService myPageService;

    @GetMapping
    @Operation(method = "GET", description = "MyPage에 들어갈 멤버 조회 API 입니다.")
    public RspTemplate<?> getMyPage(Principal principal) {
        Long memberId = Long.valueOf(principal.getName());
        Member member = memberService.findById(memberId);
        MyPageResponse myPageInfo = myPageService.getMemberInfo(member);
        return RspTemplate.success(Success.GET_MYPAGE_SUCCESS, myPageInfo);
    }
}
