package com.app.bdink.global.oauth2.apple.service;

import com.app.bdink.global.oauth2.LoginResult;
import com.app.bdink.global.oauth2.apple.response.ApplePublicKeys;
import com.app.bdink.global.oauth2.apple.verify.AppleJwtParser;
import com.app.bdink.global.oauth2.apple.verify.PublicKeyGenerator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.security.PublicKey;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppleSignInService {
    private static final String APPLE_URI = "https://appleid.apple.com/auth";
    private static final RestClient restClient = RestClient.create(APPLE_URI);
    private final AppleJwtParser appleJwtParser;
    private final PublicKeyGenerator publicKeyGenerator;

    public LoginResult getAppleId(String identityToken) {
        Map<String, String> headers = appleJwtParser.parseHeaders(identityToken);

        ResponseEntity<ApplePublicKeys> result = restClient.get()
                .uri("/keys")
                .retrieve()
                .toEntity(ApplePublicKeys.class);
        PublicKey publicKey = publicKeyGenerator.generatePublicKey(headers, result.getBody());
        Claims claims = appleJwtParser.parsePublicKeyAndGetClaims(identityToken, publicKey);
        return LoginResult.of(claims.getSubject(),null,null);
    }
}
