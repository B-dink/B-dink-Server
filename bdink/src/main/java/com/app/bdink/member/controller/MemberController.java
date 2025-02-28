package com.app.bdink.member.controller;

import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.controller.dto.request.MemberRequestDto;
import com.app.bdink.member.controller.dto.response.MemberLoginResponseDto;
import com.app.bdink.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping()
    public RspTemplate<String> join(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        memberService.join(memberRequestDto);

        return new RspTemplate<>(HttpStatus.CREATED, "회원가입");
    }

    @GetMapping()
    public RspTemplate<MemberLoginResponseDto> login(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        MemberLoginResponseDto memberLoginResponseDto = memberService.login(memberRequestDto);

        return new RspTemplate<>(HttpStatus.OK, "로그인", memberLoginResponseDto);
    }
}