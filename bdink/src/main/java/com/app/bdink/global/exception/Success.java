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
    CREATE_CHAPTER_SUCCESS(HttpStatus.CREATED, "챕터 생성 성공"),
    CREATE_INSTRUCTOR_SUCCESS(HttpStatus.CREATED, "강사 생성 성공"),
    CREATE_LECTURE_SUCCESS(HttpStatus.CREATED, "강의 생성 성공"),
    CREATE_ANSWER_SUCCESS(HttpStatus.CREATED, "답변 생성 성공"),
    CREATE_QUESTION_SUCCESS(HttpStatus.CREATED, "질문 생성 성공"),
    CREATE_SCHEDULE_SUCCESS(HttpStatus.CREATED, "스케줄 생성 성공"),
    CREATE_PRESIGNURL_SUCCESS(HttpStatus.CREATED, "presignUrl 생성 성공"),





    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입 성공"),


    /**
     * 200 OK
     */
    GET_CLASSROOM_DETAIL_SUCCESS(HttpStatus.OK, "메인 페이지 유저 정보 조회 성공"),
    GET_ALL_CLASSROOM_SUCCESS(HttpStatus.OK, "전체 클래스룸 조회 성공"),
    GET_CLASSROOM_CARRER_SUCCESS(HttpStatus.OK, "카테고리 별 클래스룸 조회 성공"),
    GET_REVIEW_SUCCESS(HttpStatus.OK, "리뷰 조회 성공"),
    GET_REVIEW_COUNT_SUCCESS(HttpStatus.OK, "리뷰 개수 조회 성공"),
    GET_QUESTION_BY_CLASS_SUCCESS(HttpStatus.OK, "클래스 별 질문 조회 성공"),
    GET_QUESTION_DETAIL_SUCCESS(HttpStatus.OK, "질문 디테일 조회 성공"),
    EMAIL_CHECK_SUCCESS(HttpStatus.OK, "이메일 체크 성공"),






    GET_BOOKMARK_SUCCESS(HttpStatus.OK, "내가 북마크한 클래스룸 조회 성공"),
    GET_CHAPTER_SUCCESS(HttpStatus.OK, "chapter 조회 성공"),
    GET_INSTRUCTOR_SUCCESS(HttpStatus.OK, "강사정보 조회 성공"),
    GET_LECTURE_SUCCESS(HttpStatus.OK, "강의 조회 성공"),




    GET_CLASSROOM_SUCCESS(HttpStatus.OK, "클래스룸 조회 성공"),
    UPDATE_CLASSROOM_SUCCESS(HttpStatus.OK, "클래스룸 조회 성공"),
    UPDATE_REVIEW_SUCCESS(HttpStatus.OK, "리뷰 수정 성공"),
    UPDATE_INSTRUCTOR_SUCCESS(HttpStatus.OK, "강사정보 수정 성공"),
    UPDATE_ANSWER_SUCCESS(HttpStatus.OK, "강사정보 수정 성공"),
    UPDATE_QUESTION_SUCCESS(HttpStatus.OK, "강사정보 수정 성공"),

    UPDATE_LECTURE_SUCCESS(HttpStatus.OK, "강의 수정 성공"),
    UPDATE_SCHEDULE_SUCCESS(HttpStatus.OK, "스케줄 수정 성공"),


    GET_CATEORY_SUCCESS(HttpStatus.OK, "세부 카테고리 조회 성공"),
    GET_ALL_SCHEDULE_SUCCESS(HttpStatus.OK, "전체 스케줄 조회 성공"),
    GET_SCHEDULE_BY_CATEGORY_SUCCESS(HttpStatus.OK, "카테고리 별 스케줄 조회 성공"),

    LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
    REVOKE_SUCCESS(HttpStatus.OK, "회원탈퇴 성공"),
    SMS_SEND_SUCCESS(HttpStatus.OK, "문자전송 성공"),
    SMS_VERIFY_SUCCESS(HttpStatus.OK, "문자인증 성공"),
    RE_ISSUE_TOKEN_SUCCESS(HttpStatus.OK, "토큰 재발급 성공"),
    TOKEN_HEALTH_CHECKED_SUCCESS(HttpStatus.OK, "토큰 헬스체크 성공"),

    DOUBLE_CHECK_SUCCESS(HttpStatus.OK, "비번 중복체크 성공"),

    SIGNOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공"),
    UPLOAD_SUCCESS(HttpStatus.OK, "업로드 성공"),

    /**
     * 202 accepted
     */
    LOGIN_ACCEPTED(HttpStatus.ACCEPTED, "소셜 로그인 통신이 성공했습니다. 다음 api를 호출해주세요."),

    /**
     * 204 NO_CONTENT
     */
    SEARCH_SUCCESS_BUT_IS_EMPTY(HttpStatus.NO_CONTENT, "검색에 성공했지만 조회된 내용이 없습니다."),
    DELETE_BOOKMARK_SUCCESS(HttpStatus.NO_CONTENT, "북마크 삭제 성공"),
    DELETE_CLASSROOM_SUCCESS(HttpStatus.NO_CONTENT, "클래스룸 삭제 성공"),
    DELETE_REVIEW_SUCCESS(HttpStatus.NO_CONTENT, "리뷰 삭제 성공"),
    DELETE_USER_SUCCESS(HttpStatus.NO_CONTENT, "회원 탈퇴가 정상적으로 이루어졌습니다."),
    DELETE_INSTRUCTOR_SUCCESS(HttpStatus.NO_CONTENT, "강사정보 삭제 성공"),
    DELETE_LECTURE_SUCCESS(HttpStatus.NO_CONTENT, "강의 삭제 성공"),
    DELETE_ANSWER_SUCCESS(HttpStatus.NO_CONTENT, "답변 삭제 성공"),
    DELETE_QUESTION_SUCCESS(HttpStatus.NO_CONTENT, "질문 삭제 성공"),
    DELETE_SCHEDULE_SUCCESS(HttpStatus.NO_CONTENT, "스케줄 삭제 성공"),



    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode(){
        return httpStatus.value();
    }

}

