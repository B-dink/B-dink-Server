package com.app.bdink.oauth2.apple.service;

import com.app.bdink.oauth2.apple.response.TokenResponse;
import com.app.bdink.oauth2.domain.LoginResult;
import com.app.bdink.oauth2.apple.response.ApplePublicKeys;
import com.app.bdink.oauth2.apple.verify.AppleJwtParser;
import com.app.bdink.oauth2.apple.verify.PublicKeyGenerator;
import com.app.bdink.oauth2.domain.SocialType;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.entity.Role;
import com.app.bdink.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import software.amazon.awssdk.http.Header;

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

    @Value("${apple-property.apple-team}")
    private String teamId;

    @Value("${apple-property.apple-private}")
    private String applePrivateKey;

    @Value("${apple-property.apple-client-id}")
    private String clientId;

    @Value("${apple-property.apple-bundle-id}")
    private String bundleId;

    @Value("${apple-property.apple-private-key}")
    private String ApplePrivateKey;

    private final MemberRepository memberRepository;

    public LoginResult getAppleId(String identityToken) {
        log.info("getAppleId: {}", identityToken);
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
            log.info("apple id {} not found", appleId);
            member = Optional.of(memberRepository.save(
                    Member.builder()
                            .name("버딩크 유저"+ UUID.randomUUID())
                            .email("")
                            .phoneNumber("")
                            .pictureUrl("")
                            .appleId(appleId)
                            .socialType(SocialType.APPLE)
                            .role(Role.SIGNUP_PROGRESS)
                            .build()));
            return LoginResult.from(member.get(), true);
        }
        log.info("apple id {} found", appleId);
        return LoginResult.from(member.get(), false);
    }

    @Transactional(readOnly = true)
    public Optional<Member> getByAppleId(String appleId){
        return memberRepository.findByAppleId(appleId);
    }

    @Deprecated
    public boolean revokeMember(String authCode, final Member member){
        String secret = publicKeyGenerator.generateClientSecret();
        log.info(secret);
        ResponseEntity<TokenResponse> tokenResponse = null;
        try {
            tokenResponse = restClient.post()
                    .uri("/token")
                    .header(Header.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(createFormData(bundleId, secret, authCode))
                    .retrieve()
                    .toEntity(TokenResponse.class);
        }catch (Exception e){
            log.info(e.getMessage());
        }

        TokenResponse body = tokenResponse.getBody();
        String refreshToken = body.refresh_token();
        log.info(refreshToken);

        ResponseEntity<?> revoke = (ResponseEntity<?>) restClient.post()
                .uri("/revoke")
                .header(Header.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(createRevoke(bundleId, secret, refreshToken))
                .retrieve();

        if (revoke.getStatusCode().is2xxSuccessful()){
            return true;
        }
        return false;

    }

    private MultiValueMap<String, String> createFormData(String clientId, String secret, String authCode) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", secret);
        formData.add("code", authCode);
        formData.add("grant_type", "authorization_code"); // grant_type 추가
        return formData;
    }

    private MultiValueMap<String, String> createRevoke(String clientId, String secret, String authCode) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", secret);
        formData.add("code", authCode);
        formData.add("token_type_hint", "refresh_token"); // grant_type 추가
        return formData;
    }

}
