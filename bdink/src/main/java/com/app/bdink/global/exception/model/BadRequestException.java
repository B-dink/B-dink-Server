package com.app.bdink.global.exception.model;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
public class BadRequestException extends CustomException{
	public BadRequestException(Error error, String message) {
		super(error, message);
	}

}
