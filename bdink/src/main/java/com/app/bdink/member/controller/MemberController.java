package com.app.bdink.member.controller;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.oauth2.controller.request.PasswordValidDto;
import com.app.bdink.global.oauth2.domain.PasswordDto;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.controller.dto.request.MemberMarketingDto;
import com.app.bdink.member.controller.dto.request.MemberPhoneUpdateRequestDto;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(method = "PATCH", description = "핸드폰 번호 수정")
    public RspTemplate<String> updatePhone(Principal principal,
                                           @RequestBody @Valid MemberPhoneUpdateRequestDto memberPhoneUpdateRequestDto) {
        memberService.updatePhoneNumber(Long.parseLong(principal.getName()), memberPhoneUpdateRequestDto);
        return RspTemplate.success(Success.UPDATE_PHONE_SUCCESS, Success.UPDATE_PHONE_SUCCESS.getMessage());
    }

    @PatchMapping("/marketing")
    @Operation(method = "PATCH", description = "마케팅 수신여부 수정")
    public RspTemplate<?> updateMarketing(Principal principal,
                                           @RequestBody MemberMarketingDto memberMarketingDto) {
        Member member = memberService.findById(Long.parseLong(principal.getName()));
        memberService.updateMarketing(member, memberMarketingDto);
        return RspTemplate.success(Success.UPDATE_MAKETING_SUCCESS, Success.UPDATE_MAKETING_SUCCESS.getMessage());
    }

    @PatchMapping("/password/reset")
    @Operation(method = "PATCH", description = "비밀번호 재설정")
    public RspTemplate<?> resetPassword(Principal principal,
                                          @RequestBody PasswordValidDto passwordDto) {
        Member member = memberService.findById(Long.parseLong(principal.getName()));
        memberService.updatePassword(member, passwordDto);
        return RspTemplate.success(Success.UPDATE_PASSWORD_SUCCESS, Success.UPDATE_PASSWORD_SUCCESS.getMessage());
    }

//    @PatchMapping("/restore-id")
//    public RspTemplate<?> restoreId(Principal principal,
//                                         @RequestBody PasswordValidDto passwordDto) {
//        Member member = memberService.findById(Long.parseLong(principal.getName()));
//        memberService.updatePassword(member, passwordDto);
//        return RspTemplate.success(Success.UPDATE_MAKETING_SUCCESS, Success.UPDATE_MAKETING_SUCCESS);
//    }
}