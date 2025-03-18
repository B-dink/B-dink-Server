package com.app.bdink.global.exception.model.net;

import lombok.Getter;
import com.app.bdink.global.exception.Error;
import java.io.IOException;

@Getter
public class CustomJavaNetException extends IOException {
	private final Error error;

	public CustomJavaNetException(Error error, String message) {
		super(message);
		this.error = error;
	}

	public int getHttpStatus() {
		return error.getErrorCode();
	}

}
