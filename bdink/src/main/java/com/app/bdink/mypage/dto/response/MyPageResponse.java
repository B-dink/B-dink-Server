package com.app.bdink.mypage.dto.response;

import com.app.bdink.member.entity.Member;
import lombok.Builder;

@Builder
public record MyPageResponse (
        Boolean isInstructor,
        // Trainer or CenterOwner
        Boolean isTrainer,
        Boolean isTrainerMembership,
        Boolean isUser,
        String memberProfile,
        String memberEmail,
        String memberName
) {
    public static MyPageResponse of(Member member, Boolean isInstructor, Boolean isTrainer,
                                    Boolean isTrainerMembership, Boolean isUser) {
        return MyPageResponse.builder()
                .isInstructor(isInstructor)
                .isTrainer(isTrainer)
                .isTrainerMembership(isTrainerMembership)
                .isUser(isUser)
                .memberEmail(member.getEmail())
                .memberProfile(member.getPictureUrl())
                .memberName(member.getName())
                .build();
    }
}
