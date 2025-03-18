package com.app.bdink.global.exception.model.net;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
public class UnknownHostException extends CustomJavaNetException {
	public UnknownHostException(Error error, String message) {
		super(error, message);
	}

}
