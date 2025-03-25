package com.app.bdink.global.oauth2.apple.verify;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.oauth2.apple.response.ApplePublicKey;
import com.app.bdink.global.oauth2.apple.response.ApplePublicKeys;
import com.app.bdink.member.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static io.jsonwebtoken.Claims.AUDIENCE;

@Component
@Slf4j
public class PublicKeyGenerator {

    private static final String SIGN_ALGORITHM_HEADER_KEY = "alg";
    private static final String KEY_ID_HEADER_KEY = "kid";
    private static final int POSITIVE_SIGN_NUMBER = 1;

    @Value("${apple-property.apple-team}")
    private String teamId;

    @Value("${apple-property.apple-private}")
    private String applePrivateKey;

    @Value("${apple-property.apple-client-id}")
    private String clientId;

    @Value("${apple-property.apple-private-key}")
    private String ApplePrivateKey;

    public PublicKey generatePublicKey(Map<String, String> headers, ApplePublicKeys applePublicKeys) {
        ApplePublicKey applePublicKey =
                applePublicKeys.getMatchesKey(headers.get(SIGN_ALGORITHM_HEADER_KEY), headers.get(KEY_ID_HEADER_KEY));
        return generatePublicKeyWithApplePublicKey(applePublicKey);
    }

    private PublicKey generatePublicKeyWithApplePublicKey(ApplePublicKey publicKey) {
        byte[] nBytes = Base64.getUrlDecoder().decode(publicKey.n());
        byte[] eBytes = Base64.getUrlDecoder().decode(publicKey.e());

        BigInteger n = new BigInteger(POSITIVE_SIGN_NUMBER, nBytes);
        BigInteger e = new BigInteger(POSITIVE_SIGN_NUMBER, eBytes);

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(publicKey.kty());
            return keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new CustomException(Error.CREATE_PUBLIC_KEY_EXCEPTION, Error.CREATE_PUBLIC_KEY_EXCEPTION.getMessage());
        }
    }

    public String generateClientSecret() {
        Date expirationDate = Date.from(LocalDateTime.now().plusMinutes(5)
                .atZone(ZoneId.systemDefault()).toInstant());
        try {
            return Jwts.builder()
                    .setHeaderParam("alg", SignatureAlgorithm.ES256)
                    .setHeaderParam("kid", applePrivateKey)
                    .setIssuer(teamId)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expirationDate)
                    .setAudience(AUDIENCE)
                    .setSubject(clientId)
                    .signWith(getSigningKey(), SignatureAlgorithm.ES256)
                    .compact();
        }catch (Exception e){
            log.info(e.getMessage());
            return "시크릿키 발급중 에러발생";
        }
    }

    private PrivateKey getSigningKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String base64 = ApplePrivateKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", ""); // 모든 공백 문자 제거
        byte[] keyBytes = Base64.getDecoder().decode(base64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(keySpec);
    }
}

