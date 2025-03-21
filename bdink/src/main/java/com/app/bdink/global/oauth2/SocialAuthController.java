package com.app.bdink.global.oauth2;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.oauth2.domain.PasswordDto;
import com.app.bdink.global.oauth2.domain.RefreshToken;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.global.token.TokenProvider;
import com.app.bdink.member.controller.dto.request.MemberRequestDto;
import com.app.bdink.member.controller.dto.response.MemberLoginRequestDto;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "회원가입/로그인 API", description = "회원가입 로그인과 관련된 API들입니다. access-refresh 토큰 형태로 진행되며, 이쪽 API를 제외하고는 다른 API들 사용시 Autorization을 통해 Bearer 형식으로 토큰을 입력받아 사용합니다.")
public class SocialAuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @GetMapping
    @Operation(method = "GET", description = "소셜 로그인(KAKAO,APPLE)을 진행해 회원가입 및 로그인을 진행합니다. ")
    public RspTemplate<?> signIn(
            @Parameter(description = "애플은 아이덴티티 토큰, 카카오는 인가코드", in = ParameterIn.QUERY) @RequestParam("code") String socialAccessToken,
            @Parameter(description = "애플은 APPLE, 카카오는 KAKAO", in = ParameterIn.QUERY) @RequestParam("provider") String provider
    ) {
        return RspTemplate.success(Success.LOGIN_SUCCESS ,authService.signIn(provider, socialAccessToken));
    }

    @PostMapping("/internal/sign-up")
    @Operation(method = "POST", description = "자체 로그인을 진행해 회원가입을 진행합니다. 진행하시기전에 더블체크 api로 패스워드 일치하는지 만들어놨습니다.")
    public RspTemplate<?> signUpInternal(
            @RequestBody @Valid MemberRequestDto memberRequestDto
    ) {
        Member member = memberService.join(memberRequestDto);
        return RspTemplate.success(Success.SIGNUP_SUCCESS, tokenProvider.createToken(member));
    }

    @PostMapping("/internal/sign-in")
    @Operation(method = "POST", description = "자체 로그인을 진행해 로그인을 진행합니다.")
    public RspTemplate<?> signInInternal(
            @RequestBody @Valid MemberLoginRequestDto memberRequestDto
    ) {
        Member member = memberService.login(memberRequestDto);
        return RspTemplate.success(Success.LOGIN_SUCCESS , tokenProvider.createToken(member));
    }

    @PostMapping("/token")
    @Operation(method = "POST", description = "리프레시토큰을 통해 엑세스, 리프레시토큰을 발급받습니다. 이 API에서 에러가 나오는 경우 리프레시도 만료된 케이스 이기 때문에 새로 로그인 하는 방식을 생각하고 있습니다.")
    public RspTemplate<?> reIssueToken(@RequestBody RefreshToken token) {
        return RspTemplate.success(Success.RE_ISSUE_TOKEN_SUCCESS ,authService.reIssueToken(token));
    }

    @PostMapping("/password/double-check")
    @Operation(method = "POST", description = "PASSWORD 확인시 일치여부를 true, false로 return 합니다.")
    public RspTemplate<?> doubleCheckPassword(@RequestBody PasswordDto passwordDto) {
        return RspTemplate.success(Success.DOUBLE_CHECK_SUCCESS , memberService.passwordDoubleCheck(passwordDto.origin(), passwordDto.copy()));
    }

    @PostMapping("/sign-out")
    @Operation(method = "POST", description = "로그아웃을 진행합니다. 엑세스토큰이 헤더 안에 포함되어있어야하며, refreshToken을 null로 만듭니다.")
    public RspTemplate<?> signOut(Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        authService.signOut(userId);
        return RspTemplate.success(Success.DELETE_USER_SUCCESS, Success.DELETE_USER_SUCCESS.getMessage());
    }


}

