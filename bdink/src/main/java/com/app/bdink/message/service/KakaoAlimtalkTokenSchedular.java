package com.app.bdink.message.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoAlimtalkTokenSchedular {

    private final KakaoAlimtalkService kakaoAlimtalkService;

    @Scheduled(fixedRate = 2700000) // 45분
    public void refreshToken() {
//        kakaoAlimtalkService.getAlimTalkToken()
//                .doOnSuccess(token -> log.info("토큰 발급 성공: {}", token))
//                .doOnError(error -> log.error("토큰 발급 실패", error))
//                .subscribe(
//                        token -> log.debug("토큰 갱신 완료"),
//                        error -> log.error("토큰 갱신 중 오류 발생", error)
//                );
    }
}
