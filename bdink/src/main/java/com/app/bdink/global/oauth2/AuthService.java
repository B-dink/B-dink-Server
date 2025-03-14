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
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppleSignInService appleSignInService;
    private final KakaoSignInService kakaoSignInService;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;


    @Transactional
    public TokenDto signIn(String socialType, String socialAccessToken) {
        LoginResult result = null;
        if (SocialType.valueOf(socialType).name().equals("APPLE")) {
            result = appleSignInService.getAppleId(socialAccessToken);
        }
        else if (SocialType.valueOf(socialType).name().equals("KAKAO")) {
            String accessToken = kakaoSignInService.getAccessToken(socialAccessToken);
            result = kakaoSignInService.loginOrSignUp(accessToken);
        }

        if (result == null){
            throw new CustomException(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage());
        }

        TokenDto tokenDto = tokenProvider.createToken(result.member());

        return tokenDto;
    }

    @Transactional
    public TokenDto reIssueToken(RefreshToken refreshToken){
        Member member = memberService.findByRefreshToken(refreshToken.refreshToken());
        return tokenProvider.reIssueTokenByRefresh(member, refreshToken.refreshToken());
    }

    @Transactional
    public void signOut(Long userId){
        Member member = memberService.findById(userId);
        member.updateRefreshToken(null);
    }


}
