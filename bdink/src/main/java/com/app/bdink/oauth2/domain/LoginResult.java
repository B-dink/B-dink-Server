package com.app.bdink.oauth2.domain;

import com.app.bdink.member.entity.Member;

public record LoginResult(Member member, boolean isNewMember) {
    public static LoginResult from(final Member member, boolean isNewMember){
        return new LoginResult(member, isNewMember);
    }
}
