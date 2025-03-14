package com.app.bdink.global.token;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.oauth2.domain.TokenDto;
import com.app.bdink.member.entity.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

// jwt를 사용하여 인증 토큰을 생성, 파싱, 검증하는 클래스
@Slf4j
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private final Key key; // jwt 서명을 위한 비밀 키. 토큰을 생성하고 검증할 때 사용
    private final long accessTokenValidityTime; // 액세스 토큰의 유효 시간 정의

    private final long refreshTokenValidityTime; // 리프레시 토큰의 유효 시간 정의

    @Autowired
    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         @Value("${jwt.access-token-validity-in-milliseconds}") long accessTokenValidityTime,
                         @Value("${jwt.refresh-token-validity-in-milliseconds}") long refreshTokenValidityTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);    // secretKey를 Base64 디코딩
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityTime = accessTokenValidityTime;
        this.refreshTokenValidityTime = refreshTokenValidityTime;
    }

    // 정보와 시크릿 키, 시간을 넣어 압축해 토큰 생성
    @Transactional
    public TokenDto createToken(Member member) {
        long nowTime = (new Date()).getTime();

        Date tokenExpiredTime = new Date(nowTime + accessTokenValidityTime); // 만료 시간 계산

        Date refreshExpiredTime = new Date(nowTime + refreshTokenValidityTime); //refreshToken 만료시간 계산

        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString()) // 토큰의 주체로 사용자의 id를 설정
                .claim(AUTHORITIES_KEY, member.getRole().name()) // 사용자 권한 정보 저장
                .setExpiration(tokenExpiredTime) // 토큰의 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // 생성된 토큰에 서명
                .compact(); // 토큰 생성

        String refreshToken = Jwts.builder()
                .setSubject(member.getId().toString()) // 토큰의 주체로 사용자의 id를 설정
                .claim(AUTHORITIES_KEY, member.getRole().name()) // 사용자 권한 정보 저장
                .setExpiration(refreshExpiredTime) // 토큰의 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // 생성된 토큰에 서명
                .compact();

        member.updateRefreshToken(refreshToken);

        return TokenDto.of(accessToken, refreshToken);
    }

    // 토큰에서 인증 정보 추출
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken); // 토큰을 파싱하여 클레임 추출

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // AUTHORITIES_KEY 라는 키를 사용해 권한 정보를 가져옴.
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")) // 권한 정보를 "ROLE_USER"와 "ROLE_ADMIN"로 분리
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
        // 위 객체는 사용자가 인증되었음을 나타내며, 이 객체를 인증 정보로 사용함.
        // 이 객체를 통해 사용자의 권한을 확인하고, 사용자가 요청할 수 있는 리소스에 접근할 수 있도록 함.
    }

    // HTTP 요청에서 토큰 추출(Bearer 라는 접두사 제거하고 실제 토큰 반환)
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization"); // HTTP 헤더에서 토큰 추출

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 문자열을 제거하고 토큰 반환
        }

        // Bearer 토큰이란: Authorization 헤더에 포함되어 사용자의 인증 상태를 서버에 전달하기 위해 사용됨.
        // 즉, 토큰이 인증 수단으로 사용됨을 나타냄.

        return null;
    }

    // 토큰 검증(토큰의 유효기간이 지났는지, 구조가 올바른지 등 체크)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key) // 서명 검증을 위해 키 설정
                    .build()
                    .parseClaimsJws(token); // 토큰을 파싱하여 서명과 유효성 검증

            return true;
        } catch (UnsupportedJwtException | ExpiredJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 클레임 파싱
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    @Transactional
    public TokenDto reIssueTokenByRefresh(Member member, String refreshToken) {
        boolean isValid = validateToken(refreshToken);

        if (!isValid){
            throw new CustomException(Error.REFRESH_TOKEN_EXPIRED_EXCEPTION, Error.REFRESH_TOKEN_EXPIRED_EXCEPTION.getMessage());
        }

        // jwt 발급 (액세스 토큰, 리프레쉬 토큰)
        TokenDto tokenDto = createToken(member);

        member.updateRefreshToken(tokenDto.refreshToken());

        return tokenDto;
    }
}
