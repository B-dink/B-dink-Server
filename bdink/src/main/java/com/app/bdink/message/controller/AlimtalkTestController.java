///*
//package com.app.bdink.message.controller;
//
//import com.app.bdink.global.template.RspTemplate;
//import com.app.bdink.message.controller.dto.AlimTalkText;
//import com.app.bdink.message.service.KakaoAlimtalkService;
//import com.app.bdink.message.controller.dto.TokenResponse;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Mono;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/v1/test/alimtalk")
//@RequiredArgsConstructor
//@Tag(name = "Alimtalk", description = "알림톡")
//public class AlimtalkTestController {
//
//    private final KakaoAlimtalkService kakaoAlimtalkService;
//
//    /**
//     * 슈어엠 토큰 발급 테스트
//     * GET /api/v1/test/surem/token
//     */
//    @GetMapping("/token")
//    @Operation(summary = "토큰 발급", description = "슈어엠 토큰 발급 테스트")
//    public Mono<RspTemplate<TokenResponse>> testIssueToken() {
//        log.info(">>> 슈어엠 토큰 발급 테스트 시작");
//        return kakaoAlimtalkService.getAlimTalkToken()
//                .doOnSuccess(response -> log.info(">>> 테스트 성공: {}", response))
//                .doOnError(error -> log.error(">>> 테스트 실패: {}", error.getMessage()));
//    }
//
//    /**
//     * 슈어엠 토큰 발급 테스트 (POST 방식)
//     * POST /api/v1/test/surem/token
//     */
//    @PostMapping("/token")
//    @Operation(summary = "토큰 발급", description = "슈어엠 토큰 발급 테스트 (POST 방식)")
//    public Mono<RspTemplate<TokenResponse>> testIssueTokenPost() {
//        log.info(">>> 슈어엠 토큰 발급 테스트 (POST) 시작");
//        return kakaoAlimtalkService.getAlimTalkToken()
//                .doOnSuccess(response -> log.info(">>> POST 테스트 성공: {}", response))
//                .doOnError(error -> log.error(">>> POST 테스트 실패: {}", error.getMessage()));
//    }
//
//    @PostMapping("/alimtalk")
//    @Operation(summary = "알림톡 발송", description = "알림톡 발송 테스트")
//    public Mono<RspTemplate<String>> testAlimtalkSend() {
//        log.info(">>> 슈어엠 알림톡 발송 테스트 (POST) 시작");
//
//        // test data
//        AlimTalkText alimTalkText = new AlimTalkText("홍길동", "신난다", "재미난다", "10만");
//
//        return kakaoAlimtalkService.sendAlimTalk("82-1041242166", alimTalkText)
//                .doOnSuccess(response -> log.info(">>> POST 테스트 성공: {}", response))
//                .doOnError(error -> log.error(">>> POST 테스트 실패: {}", error.getMessage()));
//    }
//}
//*/