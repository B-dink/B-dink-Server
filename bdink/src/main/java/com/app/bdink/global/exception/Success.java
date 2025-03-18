package com.app.bdink.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Success {

    /**
     * 201 CREATED
     */
    CREATE_BOOKMARK_SUCCESS(HttpStatus.CREATED, "북마크 생성 성공"),
    CREATE_CLASSROOM_SUCCESS(HttpStatus.CREATED, "클래스룸 생성 성공"),
    CREATE_REVIEW_SUCCESS(HttpStatus.CREATED, "리뷰 생성 성공"),


    /**
     * 200 OK
     */
    GET_USER_MAIN_SUCCESS(HttpStatus.OK, "메인 페이지 유저 정보 조회 성공"),
    GET_MYPAGE_SUCCESS(HttpStatus.OK, "마이 페이지 조회 성공"),
    GET_BOOKMARK_SUCCESS(HttpStatus.CREATED, "내가 북마크한 클래스룸 조회 성공"),


    GET_CATEORIES_SUCCESS(HttpStatus.OK, "전체 카테고리 조회 성공"),
    GET_CATEORY_SUCCESS(HttpStatus.OK, "세부 카테고리 조회 성공"),
    GET_TIMER_SUCCESS(HttpStatus.OK, "타이머 조회 성공"),
    GET_TIMER_PAGE_SUCCESS(HttpStatus.OK, "타이머 페이지 조회 성공"),
    GET_DUPLICATED_SUCCESS(HttpStatus.OK, "중복 여부 체크 성공"),

    LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
    SMS_SEND_SUCCESS(HttpStatus.OK, "문자전송 성공"),
    SMS_VERIFY_SUCCESS(HttpStatus.OK, "문자인증 성공"),
    RE_ISSUE_TOKEN_SUCCESS(HttpStatus.OK, "토큰 재발급 성공"),
    TOKEN_HEALTH_CHECKED_SUCCESS(HttpStatus.OK, "토큰 헬스체크 성공"),
    SIGNOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공"),
    DELETE_USER_SUCCESS(HttpStatus.OK, "회원 탈퇴가 정상적으로 이루어졌습니다."),


    /**
     * 204 NO_CONTENT
     */
    SEARCH_SUCCESS_BUT_IS_EMPTY(HttpStatus.NO_CONTENT, "검색에 성공했지만 조회된 내용이 없습니다."),
    DELETE_BOOKMARK_SUCCESS(HttpStatus.NO_CONTENT, "북마크 삭제 성공"),

            ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode(){
        return httpStatus.value();
    }

}

