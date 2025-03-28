package com.app.bdink.member.exception;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;

public class InvalidMemberException extends CustomException {
    public InvalidMemberException(Error error, String message) {
        super(error, message);
    }

}
