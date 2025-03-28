package com.app.bdink.member.controller;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.controller.dto.request.MemberMarketingDto;
import com.app.bdink.member.controller.dto.request.MemberPhoneUpdateRequestDto;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PatchMapping("/phone")
    public RspTemplate<String> updatePhone(Principal principal,
                                           @RequestBody @Valid MemberPhoneUpdateRequestDto memberPhoneUpdateRequestDto) {
        memberService.updatePhoneNumber(Long.parseLong(principal.getName()), memberPhoneUpdateRequestDto);
        return RspTemplate.success(Success.UPDATE_PHONE_SUCCESS, Success.UPDATE_PHONE_SUCCESS.getMessage());
    }

    @PatchMapping("/marketing")
    public RspTemplate<?> updateMarketing(Principal principal,
                                           @RequestBody MemberMarketingDto memberMarketingDto) {
        Member member = memberService.findById(Long.parseLong(principal.getName()));
        memberService.updateMarketing(member, memberMarketingDto);
        return RspTemplate.success(Success.UPDATE_MAKETING_SUCCESS, Success.UPDATE_MAKETING_SUCCESS);
    }
}