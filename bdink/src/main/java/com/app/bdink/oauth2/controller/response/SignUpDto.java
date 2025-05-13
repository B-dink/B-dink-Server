package com.app.bdink.oauth2.controller.response;

import com.app.bdink.oauth2.domain.TokenDto;

public record SignUpDto(
        TokenDto tokenDto,
        Long memberId
) {}
