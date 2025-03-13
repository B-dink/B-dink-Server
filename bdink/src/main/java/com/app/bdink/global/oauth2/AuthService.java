package com.app.bdink.global.oauth2;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.oauth2.apple.service.AppleSignInService;
import com.app.bdink.global.oauth2.domain.LoginResult;
import com.app.bdink.global.oauth2.domain.SocialType;
import com.app.bdink.global.oauth2.kakao.service.KakaoSignInService;
import com.app.bdink.global.token.Token;
import com.app.bdink.global.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppleSignInService appleSignInService;
    private final KakaoSignInService kakaoSignInService;
    private final TokenProvider tokenProvider;


    public Token signIn(String socialType, String socialAccessToken) {
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

        Token token = tokenProvider.createToken(result.member());
        return token;
    }
}
