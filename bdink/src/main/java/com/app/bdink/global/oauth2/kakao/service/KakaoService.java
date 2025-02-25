package com.app.bdink.global.oauth2.kakao.service;

import com.app.bdink.global.oauth2.kakao.dto.KakaoTokenResDto;
import com.app.bdink.global.oauth2.kakao.info.KakaoUserInfo;
import com.app.bdink.global.token.Token;
import com.app.bdink.global.token.TokenProvider;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.entity.Role;
import com.app.bdink.member.repository.MemberRepository;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com"; // 액세스 토큰 발급 서버
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com"; // 사용자 정보 서버

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    // 인가 코드를 통해 액세스 토큰을 받아오는 메서드
    public String getAccessToken(String code) {
        KakaoTokenResDto kakaoTokenResDto = WebClient.create(KAUTH_TOKEN_URL_HOST)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .bodyToMono(KakaoTokenResDto.class)
                .block();

        return kakaoTokenResDto.getAccessToken();
    }

    // 액세스 토큰을 이용해 카카오로부터 사용자 정보를 받아오는 메서드
    public KakaoUserInfo getUserInfo(String accessToken) {
        return WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block();
    }

    @Transactional
    public Token loginOrSignUp(String kakaoAccessToken) {
        KakaoUserInfo userInfo = getUserInfo(kakaoAccessToken);
        log.info(userInfo.toString());
        Long id = userInfo.getId();

        Optional<Member> member = getById(id);

        if (member.isEmpty()){
            member = Optional.of(memberRepository.save(Member.builder()
                    .name(userInfo.getKakaoAccount().getProfile().getNickName())
                    .email(userInfo.getKakaoAccount().getEmail())
                    .pictureUrl(userInfo.getKakaoAccount().getProfile().getProfileImageUrl())
                    .role(Role.ROLE_USER)
                    .build()));
        }

        return tokenProvider.createToken(member.get());
    }

    @Transactional(readOnly = true)
    public Optional<Member> getById(Long id){
        return memberRepository.findById(id);
    }
}