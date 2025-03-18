package com.app.bdink.global.exception.model;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
public class UnprocessableEntityException extends CustomException{
	public UnprocessableEntityException(Error error, String message) {
		super(error, message);
	}
}
