package com.app.bdink.oauth2.domain;

import com.app.bdink.member.entity.Member;

public record LoginResult(Member member) {
    public static LoginResult from(final Member member){
        return new LoginResult(member);
    }
}
