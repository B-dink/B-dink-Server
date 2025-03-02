package com.app.bdink.member.exception;

public class NotFoundMemberException extends RuntimeException{
    public NotFoundMemberException(final String message) {
       super(message);
    }

    public NotFoundMemberException() {
        this("회원을 찾을 수 없습니다.");
    }
}
