package com.app.bdink.global.exception;

import com.app.bdink.global.template.RspTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
@Component
@RequiredArgsConstructor
@Slf4j
public class ControllerExceptionAdvice {

    /**
     * custom error
     */
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<RspTemplate> handleCustomException(CustomException e , WebRequest request) {
        log.info("hi22");
        return ResponseEntity.status(e.getHttpStatus())
                .body(RspTemplate.error(e.getError(), e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<RspTemplate> handleConstraintDefinitionException(final MethodArgumentNotValidException e, WebRequest request) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        return ResponseEntity.status(e.getStatusCode())
                .body(RspTemplate.error(Error.BAD_REQUEST_VALIDATION, fieldError.getDefaultMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DateTimeParseException.class)
    protected ResponseEntity<RspTemplate> handleDateTimeParseException(final DateTimeParseException e, WebRequest request) {
        return ResponseEntity.status(Error.BAD_REQUEST_TIME.getErrorCode())
                .body(RspTemplate.error(Error.BAD_REQUEST_TIME, Error.BAD_REQUEST_TIME.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<RspTemplate> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e, WebRequest request) {
        return ResponseEntity.status(e.getStatusCode())
                .body(RspTemplate.error(Error.REQUEST_METHOD_VALIDATION_EXCEPTION, Error.REQUEST_METHOD_VALIDATION_EXCEPTION.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<RspTemplate> handleHttpMediaTypeNotSupportedException(final HttpMediaTypeNotSupportedException e, WebRequest request) {
        return ResponseEntity.status(e.getStatusCode())
                .body(RspTemplate.error(Error.REQUEST_MEDIA_TYPE_VALIDATION_EXCEPTION, Error.REQUEST_MEDIA_TYPE_VALIDATION_EXCEPTION.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<RspTemplate> MissingServletRequestParameterException(final MissingServletRequestParameterException e, WebRequest request) {
        return ResponseEntity.status(e.getStatusCode())
                .body(RspTemplate.error(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<RspTemplate> ServletCrawlerException(final NoResourceFoundException e, WebRequest request) {
        return ResponseEntity.status(e.getStatusCode())
                .body(RspTemplate.error(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<RspTemplate> MissingServletRequestParameterException(final HttpMessageNotReadableException e, WebRequest request) {
        return ResponseEntity.status(Error.BAD_REQUEST_VALIDATION.getErrorCode())
                .body(RspTemplate.error(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 커스텀 validation 에러 핸들링.
    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ResponseEntity<RspTemplate> ConstraintViolationException(final HandlerMethodValidationException e, WebRequest request) {
        return ResponseEntity.status(Error.BAD_REQUEST_VALIDATION.getErrorCode())
                .body(RspTemplate.error(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage()));
    }

    

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<RspTemplate> MissingRequestHeaderException(final MissingRequestHeaderException e, WebRequest request){
        return ResponseEntity.status(Error.BAD_REQUEST_VALIDATION.getErrorCode())
                .body(RspTemplate.error(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage()));
    }



    /**
     * 500 Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<RspTemplate> handleException(final Exception error, final HttpServletRequest request){
        return ResponseEntity.status(Error.INTERNAL_SERVER_ERROR.getErrorCode())
                .body(RspTemplate.error(Error.INTERNAL_SERVER_ERROR, Error.INTERNAL_SERVER_ERROR.getMessage()));    
    }

}

