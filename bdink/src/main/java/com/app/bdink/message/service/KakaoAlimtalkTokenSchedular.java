package com.app.bdink.message.service;

import com.app.bdink.message.controller.dto.TokenResponse;
import com.app.bdink.message.entity.AlimtalkToken;
import com.app.bdink.message.repository.AlimtalkTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoAlimtalkTokenSchedular {

    private final KakaoAlimtalkService kakaoAlimtalkService;
    private final KakaoAlimtalkDataService kakaoAlimtalkDataService;

//    @Scheduled(fixedRate = 2700000) // 45분
//    public void refreshToken() {
//         kakaoAlimtalkService.getAlimTalkToken()
//                 .doOnSuccess(token -> log.info("토큰 발급 성공: {}", token))
//                 .flatMap(token ->
//                         Mono.fromCallable(() -> kakaoAlimtalkDataService.saveToken(token.getData()))
//                                 .subscribeOn(Schedulers.boundedElastic())
//                 )
//                 .doOnError(error -> log.error("토큰 발급 실패", error))
//                 .subscribe(
//                         token -> log.debug("토큰 갱신 완료"),
//                         error -> log.error("토큰 갱신 중 오류 발생", error)
//                 );
//    }
}
