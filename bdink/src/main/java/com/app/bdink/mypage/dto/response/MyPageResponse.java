package com.app.bdink.mypage.dto.response;

import com.app.bdink.member.entity.Member;
import lombok.Builder;

@Builder
public record MyPageResponse (
        Boolean isInstructor,
        String memberProfile,
        String memberEmail
) {
    public static MyPageResponse of(Member member, Boolean isInstructor) {
        return MyPageResponse.builder()
                .isInstructor(isInstructor)
                .memberEmail(member.getEmail())
                .memberProfile(member.getPictureUrl())
                .build();
    }
}
