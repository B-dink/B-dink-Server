package com.app.bdink.global.exception.model;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
public class UnauthorizedException extends CustomException{
	public UnauthorizedException(Error error, String message) {
		super(error, message);
	}

}
