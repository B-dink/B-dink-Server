package com.app.bdink.member.controller.dto.response;

import com.app.bdink.oauth2.domain.SocialType;

public record SocialTypeDto(
        String socialType
) {
    public static SocialTypeDto from(SocialType socialType){
        return new SocialTypeDto(socialType.name());
    }
}
