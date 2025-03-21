package com.app.bdink.member.controller;

import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.controller.dto.request.MemberPhoneUpdateRequestDto;
import com.app.bdink.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PatchMapping("/{id}/phone")
    public RspTemplate<String> updatePhone(@PathVariable Long id,
                                           @RequestBody @Valid MemberPhoneUpdateRequestDto memberPhoneUpdateRequestDto) {
        memberService.updatePhoneNumber(id, memberPhoneUpdateRequestDto);
        return new RspTemplate<>(HttpStatus.OK, "전화번호 업데이트");
    }
}