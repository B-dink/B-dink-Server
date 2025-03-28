package com.app.bdink.global.oauth2;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.oauth2.apple.service.AppleSignInService;
import com.app.bdink.global.oauth2.domain.LoginResult;
import com.app.bdink.global.oauth2.domain.RefreshToken;
import com.app.bdink.global.oauth2.domain.SocialType;
import com.app.bdink.global.oauth2.domain.TokenDto;
import com.app.bdink.global.oauth2.kakao.service.KakaoSignInService;
import com.app.bdink.global.token.TokenProvider;
import com.app.bdink.member.controller.dto.request.MemberRequestDto;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.entity.Role;
import com.app.bdink.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AppleSignInService appleSignInService;
    private final KakaoSignInService kakaoSignInService;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;


    @Transactional
    public TokenDto signUpOrSignIn(String socialType, String socialAccessToken) {
        LoginResult result = null;

        if (SocialType.valueOf(socialType).name().equals("APPLE")) {
            result = appleSignInService.getAppleId(socialAccessToken);
        } else if (SocialType.valueOf(socialType).name().equals("KAKAO")) {
            String accessToken = kakaoSignInService.getAccessToken(socialAccessToken);
            result = kakaoSignInService.loginOrSignUp(accessToken);
        }

        if (result == null) {
            throw new CustomException(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage());
        }

        TokenDto tokenDto = tokenProvider.createToken(result.member());

        return tokenDto;
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
        return tokenProvider.reIssueTokenByRefresh(member, refreshToken.refreshToken());
    }

    @Transactional
    public void signOut(Long userId) {
        Member member = memberService.findById(userId);
        member.updateRefreshToken(null);
    }


}
