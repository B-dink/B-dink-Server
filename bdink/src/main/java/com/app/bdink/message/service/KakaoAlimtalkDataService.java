/*
package com.app.bdink.message.service;

import com.app.bdink.message.controller.dto.TokenResponse;
import com.app.bdink.message.entity.AlimtalkToken;
import com.app.bdink.message.repository.AlimtalkTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class KakaoAlimtalkDataService {

    private final AlimtalkTokenRepository alimtalkTokenRepository;

    public AlimtalkToken saveToken(TokenResponse tokenResponse) {
        AlimtalkToken tokenEntity = new AlimtalkToken(tokenResponse.data());
        return alimtalkTokenRepository.save(tokenEntity);
    }

    public AlimtalkToken getAlimtalkToken() {
        return alimtalkTokenRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> {
                    return new RuntimeException("토큰이 존재하지 않습니다");
                });
    }
}
*/