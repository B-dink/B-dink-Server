package com.app.bdink.oauth2.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.oauth2.domain.RefreshToken;
import com.app.bdink.oauth2.apple.service.AppleSignInService;
import com.app.bdink.oauth2.domain.LoginResult;
import com.app.bdink.oauth2.domain.SocialType;
import com.app.bdink.oauth2.domain.TokenDto;
import com.app.bdink.oauth2.kakao.service.KakaoSignInService;
import com.app.bdink.global.token.TokenProvider;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AppleSignInService appleSignInService;
    private final KakaoSignInService kakaoSignInService;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;


    @Transactional
    public LoginResult signUpOrSignIn(String socialType, String socialAccessToken) {
        LoginResult result = null;

        if (SocialType.valueOf(socialType).name().equals("APPLE")) {
            result = appleSignInService.getAppleId(socialAccessToken);
        } else if (SocialType.valueOf(socialType).name().equals("KAKAO")) {
            //String accessToken = kakaoSignInService.getAccessToken(socialAccessToken);
            result = kakaoSignInService.loginOrSignUp(socialAccessToken);
        }

        if (result == null) {
            throw new CustomException(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage());
        }

        return result;
    }

    // AppleSignInService.getAppleId() 메서드도 이 코드에 그대로 재사용됐는데,
    // 애플도 웹에서 쓰게 될 때 카카오처럼 코드/토큰 교환 방식이 다를 수 있어. 지금 애플 로그인은 아직 웹에서 구현 안 했으니 당장은 문제없지만,
    // 나중에 애플 웹 로그인 붙일 때 이 부분도 다시 점검 필요하다는 것을 유의하세요.

    // 웹 전용: 카카오는 인가코드 → 액세스 토큰 교환 과정이 필요함 cf) 웹은 카카오 로그인만 지원합니다. (26.7)
    @Transactional
    public LoginResult signUpOrSignInWeb(String socialType, String code, String redirectUri) {
        LoginResult result = null;

        if (SocialType.valueOf(socialType).name().equals("APPLE")) {
            result = appleSignInService.getAppleId(code); // 애플은 기존과 동일하다고 가정 (필요시 확인)
        } else if (SocialType.valueOf(socialType).name().equals("KAKAO")) {
            String accessToken = kakaoSignInService.getAccessToken(code, redirectUri);
            result = kakaoSignInService.loginOrSignUp(accessToken);
        }

        if (result == null) {
            throw new CustomException(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage());
        }

        // 웹은 프로필세팅 화면이 없으므로, 로그인 시점에 카카오 정보로 즉시 가입 완료 처리
        Member completedMember = memberService.completeWebSignUp(result.member());
        return new LoginResult(completedMember, result.isNewMember());
    }

    @Transactional
    public void revoke(Principal principal, String socialType) {
        Long id = Long.parseLong(principal.getName());
        Member member = memberService.findById(id);
         if (SocialType.valueOf(socialType).name().equals("KAKAO")) {
            kakaoSignInService.revokeMember(memberService.findById(id));
        }
        try {
            memberService.deleteMember(member);
        } catch (Exception e) {
            log.info("회원탈퇴 jpa 에러발생. + id=" + id);
            throw new CustomException(Error.INTERNAL_SERVER_ERROR, Error.INTERNAL_SERVER_ERROR.getMessage());
        }
    }


    @Transactional
    public TokenDto reIssueToken(RefreshToken refreshToken) {
        Member member = memberService.findByRefreshToken(refreshToken.refreshToken());
        TokenDto tokenDto = tokenProvider.reIssueTokenByRefresh(member, refreshToken.refreshToken());
        memberService.markLogin(member);
        return tokenDto;
    }

    @Transactional
    public void signOut(Long userId) {
        Member member = memberService.findById(userId);
        member.updateRefreshToken(null);
    }


}
