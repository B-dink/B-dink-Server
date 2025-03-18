package com.app.bdink.global.exception.model;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
public class NotFoundException extends CustomException {
	public NotFoundException(Error error, String message) {
		super(error, message);
	}
}
