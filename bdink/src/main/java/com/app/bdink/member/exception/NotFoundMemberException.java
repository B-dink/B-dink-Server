package com.app.bdink.member.exception;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;

public class NotFoundMemberException extends CustomException {
    public NotFoundMemberException(Error error, String message) {
        super(error, message);
    }
}
