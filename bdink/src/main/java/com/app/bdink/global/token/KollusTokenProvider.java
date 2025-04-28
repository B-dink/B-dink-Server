package com.app.bdink.global.token;

import com.app.bdink.external.kollus.dto.KollusTokenDTO;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Base64.getUrlEncoder;

@Slf4j
@Component
public class KollusTokenProvider {
    private final String kollusKey;
    private final long kollusAccessTokenValidityTime;

    @Autowired
    public KollusTokenProvider(
            @Value("${kollus.SECRET_KEY}") String kollusSecretKey,
            @Value("${jwt.kollus-access-token-validity-in-milliseconds}") long kollusAccessTokenValidityTime) {
        this.kollusKey = kollusSecretKey;
        this.kollusAccessTokenValidityTime = kollusAccessTokenValidityTime;
    }

    @Transactional
    public KollusTokenDTO createKollusJwtToken(String clientUserId, String mediacontentkey) {
        try {
            long nowTime = System.currentTimeMillis() / 1000L;  //현재 시간(초)
            long expiry = nowTime + (kollusAccessTokenValidityTime / 1000L) + 60L; //Kollus쪽 공식가이드를 따라 1분 이내까지 허용하게 생성

            // 1. Header 구성
            Map<String, Object> header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");

            // 2. Payload 구성
            Map<String, Object> payload = new HashMap<>();
            payload.put("cuid", clientUserId);
            payload.put("expt", expiry); // Kollus 요구 스펙: 만료시간은 '초' 단위
            payload.put("mc", List.of(Map.of("mckey", mediacontentkey)));

            // 3. Base64Url 인코딩
            String encodedHeader = base64UrlEncode(JsonUtil.toJson(header));
            String encodedPayload = base64UrlEncode(JsonUtil.toJson(payload));

            String unsignedToken = encodedHeader + "." + encodedPayload;

            // 4. Signature 생성 (HMAC-SHA256)
            String signature = hmacSha256(unsignedToken, kollusKey);

            // 5. 최종 JWT
            String jwt = unsignedToken + "." + signature;

            return KollusTokenDTO.of(jwt);

        } catch (Exception e) {
            log.error("Kollus JWT 생성 실패", e);
            throw new CustomException(Error.UNPROCESSABLE_JWT_CREATE_EXCEPTION, Error.UNPROCESSABLE_JWT_CREATE_EXCEPTION.getMessage());
        }
    }

    // Base64Url Encoding (Padding 제거)
    private String base64UrlEncode(String data) {
        return getUrlEncoder().withoutPadding()
                .encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    // HMAC-SHA256 서명 생성
    private String hmacSha256(String data, String secretKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hmacBytes);
    }
}
