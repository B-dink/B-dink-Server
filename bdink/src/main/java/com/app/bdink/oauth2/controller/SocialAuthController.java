package com.app.bdink.oauth2.controller;

import com.app.bdink.external.aws.service.S3Service;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.global.token.TokenProvider;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.member.controller.dto.request.MemberSocialRequestDto;
import com.app.bdink.member.controller.dto.response.NameCheckDto;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.oauth2.controller.response.SignUpDto;
import com.app.bdink.oauth2.domain.LoginResult;
import com.app.bdink.oauth2.domain.RefreshToken;
import com.app.bdink.oauth2.domain.TokenDto;
import com.app.bdink.oauth2.service.AuthService;
import com.app.bdink.qna.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth2")
@Slf4j
@Tag(name = "회원가입/로그인 API", description = "회원가입 로그인과 관련된 API들입니다. access-refresh 토큰 형태로 진행되며, 이쪽 API를 제외하고는 다른 API들 사용시 Autorization을 통해 Bearer 형식으로 토큰을 입력받아 사용합니다.")
public class SocialAuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final MemberUtilService memberUtilService;
    private final S3Service s3Service;
    private final QuestionService questionService;

    @GetMapping
    @Operation(method = "GET", description = "소셜 로그인(KAKAO,APPLE)을 진행해 회원가입 및 로그인을 진행합니다. 소셜 로그인은 자체 회원가입으로 넘어갑니다. 여기서 넘겨주는 토큰을 social-signup으로 넘겨주세요.")
    public RspTemplate<?> signIn(
            @Parameter(description = "애플은 아이덴티티 토큰, 카카오는 인가코드", in = ParameterIn.QUERY) @RequestParam("code") String socialAccessToken,
            @Parameter(description = "애플은 APPLE, 카카오는 KAKAO", in = ParameterIn.QUERY) @RequestParam("provider") String provider
    ) {
        LoginResult result = authService.signUpOrSignIn(provider, socialAccessToken);
        TokenDto tokenDto = tokenProvider.createToken(result.member());
        SignUpDto signUpDto = new SignUpDto(tokenDto, result.member().getId());
        if (result.isNewMember()) {
            return RspTemplate.success(Success.LOGIN_ACCEPTED, signUpDto);
        }
        return RspTemplate.success(Success.SIGNUP_SUCCESS, signUpDto);
    }

    @DeleteMapping("/revoke")
    @Operation(method = "DELETE", description = "소셜로그인 회원탈퇴를 진행합니다. authcode는 애플로그인인 경우만 필요.")
    public RspTemplate<?> revoke(@RequestParam String provider, Principal principal) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        Instructor instructor = member.getInstructor();
        if (instructor != null) { // 강사의 경우 막아야함.
            log.info("강사발생");
            throw new CustomException(Error.UNPROCESSABLE_ENTITY_DELETE_EXCEPTION, Error.UNPROCESSABLE_ENTITY_DELETE_EXCEPTION.getMessage());
        }
        questionService.revokeUserDeleteQuestion(member);
        authService.revoke(principal, provider);
        return RspTemplate.success(Success.REVOKE_SUCCESS);
    }

//    @PostMapping("/internal/sign-up")
//    @Deprecated
//    @Operation(method = "POST", description = "자체 로그인을 진행해 회원가입을 진행합니다.")
//    public RspTemplate<?> signUpInternal(
//            @RequestBody @Valid MemberRequestDto memberRequestDto
//    ) {
//        Member member = memberService.join(memberRequestDto);
//        return RspTemplate.success(Success.SIGNUP_SUCCESS, tokenProvider.createToken(member));
//    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/social/sign-up")
    @Operation(method = "POST", description = "in-progress인 소셜 로그인 유저를 회원가입을 완료시킵니다.")
    public RspTemplate<?> signUpSocial(
            Principal principal,
            @Valid @RequestPart(value = "memberSocialRequestDto") MemberSocialRequestDto memberRequestDto,
            @RequestPart(value = "profile") MultipartFile profileImage
    ) {
        Member member = memberService.findById(Long.parseLong(principal.getName()));
        String image = s3Service.uploadImageOrMedia("image/", profileImage);
        member = memberService.socialJoin(member, memberRequestDto, image);
        return RspTemplate.success(Success.SIGNUP_SUCCESS, tokenProvider.createToken(member));
    }


//    @PostMapping("/internal/sign-in")
//    @Operation(method = "POST", description = "자체 로그인을 진행해 로그인을 진행합니다.")
//    public RspTemplate<?> signInInternal(
//            @RequestBody @Valid MemberLoginRequestDto memberRequestDto
//    ) {
//        Member member = memberService.login(memberRequestDto);
//        return RspTemplate.success(Success.LOGIN_SUCCESS , tokenProvider.createToken(member));
//    }

    @PostMapping("/token")
    @Operation(method = "POST", description = "리프레시토큰을 통해 엑세스, 리프레시토큰을 발급받습니다. 이 API에서 에러가 나오는 경우 리프레시도 만료된 케이스 이기 때문에 새로 로그인 하는 방식을 생각하고 있습니다.")
    public RspTemplate<?> reIssueToken(@RequestBody RefreshToken token) {
        return RspTemplate.success(Success.RE_ISSUE_TOKEN_SUCCESS, authService.reIssueToken(token));
    }

//    @PostMapping("/password/double-check")
//    @Deprecated
//    @Operation(method = "POST", description = "PASSWORD 확인시 일치여부를 true, false로 return 합니다.")
//    public RspTemplate<?> doubleCheckPassword(@RequestBody PasswordDto passwordDto) {
//        return RspTemplate.success(Success.DOUBLE_CHECK_SUCCESS , memberService.passwordDoubleCheck(passwordDto.origin(), passwordDto.copy()));
//    }

//    @PostMapping("/password/check")
//    @Deprecated
//    @Operation(method = "POST", description = "PASSWORD가 규칙에 맞게 작성되었는지 검증합니다.")
//    public RspTemplate<?> checkPassword(@RequestBody PasswordValidDto passwordDto) {
//        int status = memberService.passwordCheck(passwordDto);
//        if (status == 200){
//            return RspTemplate.success(Success.PASSWORD_IS_VALID, DoubleCheckResponse.from(true));
//        }
//        return RspTemplate.error(status, Error.BAD_REQUEST_PASSWORD.getMessage());
//    }

    @GetMapping("/name")
    @Operation(method = "GET", description = "이름 검증 api")
    public RspTemplate<?> checkName(@RequestParam String name) {

        NameCheckDto result = memberService.checkName(name);

        if (result.isCorrect()) {
            return RspTemplate.success(Success.NAME_CHECK_SUCCESS, result);
        } else
            return RspTemplate.success(Success.NAME_CHECK_DUPLICATED, result);
    }

    @PostMapping("/sign-out")
    @Operation(method = "POST", description = "로그아웃을 진행합니다. 엑세스토큰이 헤더 안에 포함되어있어야하며, refreshToken을 null로 만듭니다.")
    public RspTemplate<?> signOut(Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        authService.signOut(userId);
        return RspTemplate.success(Success.DELETE_USER_SUCCESS, Success.DELETE_USER_SUCCESS.getMessage());
    }


}

