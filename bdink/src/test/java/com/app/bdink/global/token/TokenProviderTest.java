package com.app.bdink.global.token;

import com.app.bdink.external.kollus.dto.KollusTokenDTO;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class TokenProviderTest {

    private static final Logger log = LoggerFactory.getLogger(TokenProviderTest.class);

    SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

    long kollusAccessTokenValidity = 1000 * 60 * 30; // 30분

    private final KollusTokenProvider tokenProvider = new KollusTokenProvider(
            encodedKey,
            kollusAccessTokenValidity
    );

    @Test
    void testCreateKollusJwtToken() {
        String userKey = "test-user-key";
        String mediaContentKey = "media-abc-123";

        KollusTokenDTO kollusTokenDTO = tokenProvider.createKollusJwtToken(userKey, mediaContentKey);

        assertNotNull(kollusTokenDTO, "KollusTokenDTO should not be null");
        assertNotNull(kollusTokenDTO.kollusAccessToken(), "AccessToken should not be null");

        log.info("생성된 Kollus 토큰: {}", kollusTokenDTO.kollusAccessToken());
    }
}
