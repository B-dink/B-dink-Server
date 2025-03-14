package com.app.bdink.global.oauth2.apple.service;

import com.app.bdink.global.oauth2.domain.LoginResult;
import com.app.bdink.global.oauth2.apple.response.ApplePublicKeys;
import com.app.bdink.global.oauth2.apple.verify.AppleJwtParser;
import com.app.bdink.global.oauth2.apple.verify.PublicKeyGenerator;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.entity.Role;
import com.app.bdink.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.security.PublicKey;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppleSignInService {
    private static final String APPLE_URI = "https://appleid.apple.com/auth";
    private static final RestClient restClient = RestClient.create(APPLE_URI);
    private final AppleJwtParser appleJwtParser;
    private final PublicKeyGenerator publicKeyGenerator;

    private final MemberRepository memberRepository;

    public LoginResult getAppleId(String identityToken) {
        Map<String, String> headers = appleJwtParser.parseHeaders(identityToken);

        ResponseEntity<ApplePublicKeys> result = restClient.get()
                .uri("/keys")
                .retrieve()
                .toEntity(ApplePublicKeys.class);
        PublicKey publicKey = publicKeyGenerator.generatePublicKey(headers, result.getBody());
        Claims claims = appleJwtParser.parsePublicKeyAndGetClaims(identityToken, publicKey);

        String appleId = claims.getSubject();

        Optional<Member> member = getByAppleId(appleId);

        if (member.isEmpty()){
            member = Optional.of(memberRepository.save(
                    Member.builder()
                            .name("버딩크 유저"+ UUID.randomUUID())
                            .email("")
                            .phoneNumber("")
                            .pictureUrl("")
                            .appleId(appleId)
                            .role(Role.ROLE_USER)
                            .build()));
        }
        return LoginResult.from(member.get());
    }

    @Transactional(readOnly = true)
    public Optional<Member> getByAppleId(String appleId){
        return memberRepository.findByAppleId(appleId);
    }
}
