package com.app.bdink.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Error {

    /**
     * 404 NOT FOUND
     */
    NOT_FOUND_USER_EXCEPTION(HttpStatus.NOT_FOUND, "찾을 수 없는 유저입니다."),
    NOT_FOUND_QUESTION(HttpStatus.NOT_FOUND, "찾을 수 없는 질문입니다."),
    NOT_FOUND_REPORT_TYPE(HttpStatus.NOT_FOUND, "찾을 수 없는 신고유형입니다."),

    NOT_FOUND_ANSWER(HttpStatus.NOT_FOUND, "찾을 수 없는 답변입니다."),
    NOT_FOUND_BOOKMARK(HttpStatus.NOT_FOUND, "찾을 수 없는 북마크입니다."),
    NOT_FOUND_SUGANG(HttpStatus.NOT_FOUND, "찾을 수 없는 수강입니다."),

    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "찾을 수 없는 리뷰입니다."),
    NOT_FOUND_SCHEDULE(HttpStatus.NOT_FOUND, "찾을 수 없는 일정입니다."),
    NOT_FOUND_CLASSROOM(HttpStatus.NOT_FOUND, "찾을 수 없는 강의실입니다."),
    NOT_FOUND_CLASSROOM_DETAILPAGE(HttpStatus.NOT_FOUND, "강의실에 디테일 페이지가 없습니다."),
    NOT_FOUND_CHAPTER(HttpStatus.NOT_FOUND, "찾을 수 없는 챕터입니다."),
    NOT_FOUND_LECTURE(HttpStatus.NOT_FOUND, "찾을 수 없는 강의입니다."),
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, "찾을 수 없는 파일입니다."),
    NOT_FOUND_INSTRUCTOR(HttpStatus.NOT_FOUND, "찾을 수 없는 강사입니다."),
    NOT_FOUND_USERKEY(HttpStatus.NOT_FOUND, "찾을 수 없는 사용자키 입니다."),

    /**
     * 400 BAD REQUEST EXCEPTION
     */
    BAD_REQUEST_ID(HttpStatus.BAD_REQUEST, "잘못된 id값입니다."),
    BAD_REQUEST_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 패스워드 요청"),
    BAD_REQUEST_VALIDATION(HttpStatus.BAD_REQUEST, "유효한 값으로 요청을 다시 보내주세요."),
    BAD_REQUEST_PROVIDER(HttpStatus.BAD_REQUEST, "다른 유형 로그인으로 가입하셨습니다."),
    BAD_REQUEST_EMAIL(HttpStatus.BAD_REQUEST, "사용할 수 없는 이메일입니다."),
    EXIST_USER(HttpStatus.BAD_REQUEST, "이미 가입한 유저입니다."),
    EXIST_USERKEY(HttpStatus.BAD_REQUEST, "이미 유저키가 존재합니다."),
    NOT_EXIST_USERKEY(HttpStatus.BAD_REQUEST, "남아있는 사용자 키가 없습니다. 사용자키를 새로 추가해주세요."),

    BAD_REQUEST_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "파일형식이 잘못된 것 같습니다."),
    BAD_REQUEST_FILE_SIZE(HttpStatus.BAD_REQUEST, "파일크기가 잘못된 것 같습니다. 최대 5MB"),
    INVALID_APPLE_PUBLIC_KEY(HttpStatus.BAD_REQUEST, "유효하지않은 애플 퍼블릭 키 입니다."),
    INVALID_APPLE_IDENTITY_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 아이덴티티 토큰입니다."),
    BAD_REQUEST_TIME(HttpStatus.BAD_REQUEST, "시간 형식이 다릅니다." ),
    REQUEST_MEDIA_TYPE_VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "미디어 타입이 맞지않습니다. 동영상 .mp4파일만 보내주세요."),
    REQUEST_METHOD_VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "메소드가 틀립니다."),
    EXIST_BOOKMARK(HttpStatus.BAD_REQUEST, "이미 북마크한 강의입니다."),
    EXIST_REVIEW(HttpStatus.BAD_REQUEST, "이미 리뷰한 강의입니다."),

    /**
     * 401 UNAUTHORIZED EXCEPTION
     */
    TOKEN_TIME_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    VERIFY_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 인증입니다. 재발송 해주세요."),
    REFRESH_TOKEN_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다. 재로그인해주세요."),
    EXPIRED_APPLE_IDENTITY_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 아이덴티티 토큰입니다."),
    UN_AUTHORIZED(HttpStatus.UNAUTHORIZED, "토큰 인증을 안한 유저입니다."),

    /**
     * 402 PAYMENT REQUIRED
     */

    INVALID_CODE_EXCEPTION(HttpStatus.UNAUTHORIZED, "인증 코드가 일치하지 않습니다."),

    /**
     * 403 FORBIDDEN EXCEPTION
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, "리소스에 대한 권한이 없습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN,"리소스에 접근할 수 없는 유저입니다. 토큰을 확인해주세요."),
    NO_INSTRUCTOR(HttpStatus.FORBIDDEN, "강사정보가 없는 유저입니다."),
    INVALID_USER_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없는 유저입니다."),

    /**
     * 422 UNPROCESSABLE_ENTITY
     */
    UNPROCESSABLE_ENTITY_DELETE_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY, "서버에서 요청을 이해해 삭제하려는 도중 문제가 생겼습니다."),
    UNPROCESSABLE_INSTRUCTOR_DELETE_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY, "강사는 계약 양도 이후에 삭제할 수 있습니다."),
    UNPROCESSABLE_KAKAO_SERVER_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY, "카카오서버와 통신 중 오류가 발생했습니다."),
    UNPROCESSABLE_APPLE_SERVER_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY, "카카오서버와 통신 중 오류가 발생했습니다."),
    UNPROCESSABLE_JWT_CREATE_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY, "서버에서 KOLLUS JWT토큰을 생성 중 오류가 발생했습니다."),


    /**
     * 500 INTERNAL_SERVER_ERROR
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러가 발생했습니다"),
    INVALID_ENCRYPT_COMMUNICATION(HttpStatus.INTERNAL_SERVER_ERROR, "ios 통신 증명 과정 중 문제가 발생했습니다."),
    CREATE_PUBLIC_KEY_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "publickey 생성 과정 중 문제가 발생했습니다."),
    ;


    private final HttpStatus httpStatus;
    private final String message;

    public int getErrorCode() {
        return httpStatus.value();
    }
}

