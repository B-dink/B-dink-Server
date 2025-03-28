package com.app.bdink.global.template;

import com.app.bdink.global.exception.Error;
import com.app.bdink.global.exception.Success;
import lombok.*;
import org.springframework.http.HttpStatus;

// 응답 템플릿
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class RspTemplate<T> {
    private final int statusCode;
    private final String message;
    T data;

    public RspTemplate(HttpStatus httpStatus, String message, T data) {
        this.statusCode = httpStatus.value();
        this.message = message;
        this.data = data;
    }

    public RspTemplate(HttpStatus httpStatus, String message) {
        this.statusCode = httpStatus.value();
        this.message = message;
    }

    public static RspTemplate success(Success success){
        return new RspTemplate<>(success.getHttpStatusCode(), success.getMessage());
    }

    public static <T> RspTemplate<T> success(Success success, T data){
        return new RspTemplate<T>(success.getHttpStatusCode(), success.getMessage(), data);
    }

    public static RspTemplate error(Error error){
        return new RspTemplate<>(error.getErrorCode(), error.getMessage());
    }

    public static RspTemplate error(Error error, String message){
        return new RspTemplate<>(error.getErrorCode(), message);
    }

    public static RspTemplate error(int customCode, String message){
        return new RspTemplate<>(customCode, message);
    }
}
