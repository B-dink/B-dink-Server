package com.app.bdink.global.oauth2.kakao.controller;

import com.app.bdink.global.oauth2.kakao.service.KakaoService;
import com.app.bdink.global.token.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/callback")
@Slf4j
public class KakaoLoginController {

    private final KakaoService kakaoService;

    // 로그인 및 회원가입
    @GetMapping
    public @ResponseBody Token kakaoLogin(@RequestParam("code") String code) {
        String kakaoAccessToken = kakaoService.getAccessToken(code);
        Token token = kakaoService.loginOrSignUp(kakaoAccessToken);
        log.info("로그인 성공 !");
        return token;
    }
}
