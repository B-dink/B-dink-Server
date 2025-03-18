package com.app.bdink.global.exception.model;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;

public class ForbiddenException extends CustomException {
	public ForbiddenException(Error error, String message) {
		super(error, message);
	}

}