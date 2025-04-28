package com.app.bdink.global.exception.model;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import lombok.Getter;

@Getter
public class PaymentFailedException extends CustomException {
    public PaymentFailedException(Error error, String message) {
        super(error, message);
    }

}
