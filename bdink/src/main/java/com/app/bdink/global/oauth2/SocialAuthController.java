package com.app.bdink.global.oauth2;

import com.app.bdink.global.oauth2.domain.PasswordDto;
import com.app.bdink.global.oauth2.domain.RefreshToken;
import com.app.bdink.global.token.TokenProvider;
import com.app.bdink.member.controller.dto.request.MemberRequestDto;
import com.app.bdink.member.controller.dto.response.MemberLoginRequestDto;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth2")
public class SocialAuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @GetMapping
    public ResponseEntity<?> signIn(
            @RequestParam("code") String socialAccessToken,
            @RequestParam("provider") String provider
    ) {
        return ResponseEntity.ok(authService.signIn(provider, socialAccessToken));
    }

    @PostMapping("/internal/sign-up")
    public ResponseEntity<?> signUpInternal(
            @RequestBody @Valid MemberRequestDto memberRequestDto
    ) {
        Member member = memberService.join(memberRequestDto);
        return ResponseEntity.ok(tokenProvider.createToken(member));
    }

    @PostMapping("/internal/sign-in")
    public ResponseEntity<?> signInInternal(
            @RequestBody @Valid MemberLoginRequestDto memberRequestDto
    ) {
        Member member = memberService.login(memberRequestDto);
        return ResponseEntity.ok(tokenProvider.createToken(member));
    }

    @PostMapping("/token")
    public ResponseEntity<?> reIssueToken(@RequestBody RefreshToken token) {
        return ResponseEntity.ok(authService.reIssueToken(token));
    }

    @PostMapping("/password/double-check")
    public ResponseEntity<?> doubleCheckPassword(@RequestBody PasswordDto passwordDto) {
        return ResponseEntity.ok(memberService.passwordDoubleCheck(passwordDto.origin(), passwordDto.copy()));
    }

    @PostMapping("/sign-out")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> signOut(Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        authService.signOut(userId);
        return ResponseEntity.noContent().build();
    }


}

