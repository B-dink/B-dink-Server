package com.app.bdink.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

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
    NOT_FOUND_SUGANGLIST(HttpStatus.NOT_FOUND, "수강 신청이 되어있지 않은 유저입니다."),

    NOT_FOUND_CENTER(HttpStatus.NOT_FOUND, "찾을 수 없는 센터입니다."),
    NOT_FOUND_EXERCISE(HttpStatus.NOT_FOUND, "찾을 수 없는 운동종목입니다."),
    NOT_FOUND_WORKOUTSESSION(HttpStatus.NOT_FOUND, "해당 회원의 운동일지를 찾을 수 없습니다."),

    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "찾을 수 없는 리뷰입니다."),
    NOT_FOUND_SCHEDULE(HttpStatus.NOT_FOUND, "찾을 수 없는 일정입니다."),
    NOT_FOUND_CLASSROOM(HttpStatus.NOT_FOUND, "찾을 수 없는 강의실입니다."),
    NOT_FOUND_CLASSROOM_DETAILPAGE(HttpStatus.NOT_FOUND, "강의실에 디테일 페이지가 없습니다."),
    NOT_FOUND_CHAPTER(HttpStatus.NOT_FOUND, "찾을 수 없는 챕터입니다."),
    NOT_FOUND_LECTURE(HttpStatus.NOT_FOUND, "찾을 수 없는 강의입니다."),
    NOT_FOUND_KOLLUSMEDIA(HttpStatus.NOT_FOUND, "찾을 수 없는 미디어입니다."),
    NOT_FOUND_KOLLUSMEDIALINK(HttpStatus.NOT_FOUND, "찾을 수 없는 시청기록 입니다."),
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, "찾을 수 없는 파일입니다."),
    NOT_FOUND_INSTRUCTOR(HttpStatus.NOT_FOUND, "찾을 수 없는 강사입니다."),
    NOT_FOUND_YOUTUBEVIDEO(HttpStatus.NOT_FOUND, "찾을 수 없는 유튜브 비디오 입니다."),

    NOT_FOUND_PAYMENT(HttpStatus.NOT_FOUND, "존재하지 않는 결제 정보 입니다."),
    NOT_FOUND_PAYMENT_SESSION(HttpStatus.NOT_FOUND, "결제 시간이 만료되어 결제 진행 데이터가 존재하지 않습니다."),
    FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING(HttpStatus.NOT_FOUND, "결제가 완료되지 않았어요. 다시 시도해주세요."),
    FAILED_INTERNAL_SYSTEM_PROCESSING(HttpStatus.NOT_FOUND, "내부 시스템 처리 작업이 실패했습니다. 잠시 후 다시 시도해주세요."),
    UNKNOWN_PAYMENT_ERROR(HttpStatus.NOT_FOUND, "결제에 실패했어요. 같은 문제가 반복된다면 은행이나 카드사로 문의해주세요."),
    NOT_FOUND_USERKEY(HttpStatus.NOT_FOUND, "찾을 수 없는 사용자키 입니다."),

    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 정보 입니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다"),

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
    EXIST_SUGANG(HttpStatus.BAD_REQUEST, "이미 수강신청한 강의 입니다."),
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
    EXIST_KOLLUSMEDIALINK(HttpStatus.BAD_REQUEST, "이미 생성된 시청기록입니다."),

    ALREADY_PROCESSED_PAYMENT(HttpStatus.BAD_REQUEST, "이미 처리된 결제 입니다."),
    PROVIDER_ERROR(HttpStatus.BAD_REQUEST, "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    EXCEED_MAX_CARD_INSTALLMENT_PLAN(HttpStatus.BAD_REQUEST, "설정 가능한 최대 할부 개월 수를 초과했습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_ALLOWED_POINT_USE(HttpStatus.BAD_REQUEST, "포인트 사용이 불가한 카드로 카드 포인트 결제에 실패했습니다."),
    INVALID_API_KEY(HttpStatus.BAD_REQUEST, "잘못된 시크릿키 연동 정보 입니다."),
    INVALID_REJECT_CARD(HttpStatus.BAD_REQUEST, "카드 사용이 거절되었습니다. 카드사 문의가 필요합니다."),
    BELOW_MINIMUM_AMOUNT(HttpStatus.BAD_REQUEST, "신용카드는 결제금액이 100원 이상, 계좌는 200원이상부터 결제가 가능합니다."),
    INVALID_CARD_EXPIRATION(HttpStatus.BAD_REQUEST, "카드 정보를 다시 확인해주세요. (유효기간)"),
    INVALID_STOPPED_CARD(HttpStatus.BAD_REQUEST, "정지된 카드 입니다."),
    EXCEED_MAX_DAILY_PAYMENT_COUNT(HttpStatus.BAD_REQUEST, "하루 결제 가능 횟수를 초과했습니다."),
    NOT_SUPPORTED_INSTALLMENT_PLAN_CARD_OR_MERCHANT(HttpStatus.BAD_REQUEST, "할부가 지원되지 않는 카드 또는 가맹점 입니다."),
    INVALID_CARD_INSTALLMENT_PLAN(HttpStatus.BAD_REQUEST, "할부 개월 정보가 잘못되었습니다."),
    NOT_SUPPORTED_MONTHLY_INSTALLMENT_PLAN(HttpStatus.BAD_REQUEST, "할부가 지원되지 않는 카드입니다."),
    EXCEED_MAX_PAYMENT_AMOUNT(HttpStatus.BAD_REQUEST, "하루 결제 가능 금액을 초과했습니다."),
    NOT_FOUND_TERMINAL_ID(HttpStatus.BAD_REQUEST, "단말기번호(Terminal Id)가 없습니다. 토스페이먼츠로 문의 바랍니다."),
    INVALID_AUTHORIZE_AUTH(HttpStatus.BAD_REQUEST, "유효하지 않은 인증 방식입니다."),
    INVALID_CARD_LOST_OR_STOLEN(HttpStatus.BAD_REQUEST, "분실 혹은 도난 카드입니다."),
    RESTRICTED_TRANSFER_ACCOUNT(HttpStatus.BAD_REQUEST, "계좌는 등록 후 12시간 뒤부터 결제할 수 있습니다. 관련 정책은 해당 은행으로 문의해주세요."),
    INVALID_CARD_NUMBER(HttpStatus.BAD_REQUEST, "카드번호를 다시 확인해주세요."),
    INVALID_UNREGISTERED_SUBMALL(HttpStatus.BAD_REQUEST, "등록되지 않은 서브몰입니다. 서브몰이 없는 가맹점이라면 안심클릭이나 ISP 결제가 필요합니다."),
    NOT_REGISTERED_BUSINESS(HttpStatus.BAD_REQUEST, "등록되지 않은 사업자 번호입니다."),
    EXCEED_MAX_ONE_DAY_WITHDRAW_AMOUNT(HttpStatus.BAD_REQUEST, "1일 출금 한도를 초과했습니다."),
    EXCEED_MAX_ONE_TIME_WITHDRAW_AMOUNT(HttpStatus.BAD_REQUEST, "1회 출금 한도를 초과했습니다."),
    CARD_PROCESSING_ERROR(HttpStatus.BAD_REQUEST, "카드사에서 오류가 발생했습니다."),
    EXCEED_MAX_AMOUNT(HttpStatus.BAD_REQUEST, "거래금액 한도를 초과했습니다."),
    INVALID_ACCOUNT_INFO_RE_REGISTER(HttpStatus.BAD_REQUEST, "유효하지 않은 계좌입니다. 계좌 재등록 후 시도해주세요."),
    NOT_AVAILABLE_PAYMENT(HttpStatus.BAD_REQUEST, "결제가 불가능한 시간대입니다."),
    UNAPPROVED_ORDER_ID(HttpStatus.BAD_REQUEST, "아직 승인되지 않은 주문번호입니다."),
    EXCEED_MAX_MONTHLY_PAYMENT_AMOUNT(HttpStatus.BAD_REQUEST, "당월 결제 가능금액인 1,000,000원을 초과 하셨습니다."),

    ALREADY_CANCELED_PAYMENT(HttpStatus.BAD_REQUEST, "이미 취소된 결제 입니다."),
    INVALID_REFUND_ACCOUNT_INFO(HttpStatus.BAD_REQUEST, "환불 계좌번호와 예금주명이 일치하지 않습니다."),
    EXCEED_CANCEL_AMOUNT_DISCOUNT_AMOUNT(HttpStatus.BAD_REQUEST, "즉시할인금액보다 적은 금액은 부분취소가 불가능합니다."),
    INVALID_REFUND_ACCOUNT_NUMBER(HttpStatus.BAD_REQUEST, "잘못된 환불 계좌번호입니다."),
    REFUND_REJECTED(HttpStatus.BAD_REQUEST, "환불이 거절됐습니다. 결제사에 문의 부탁드립니다."),
    ALREADY_REFUND_PAYMENT(HttpStatus.BAD_REQUEST, "이미 환불된 결제입니다."),
    FORBIDDEN_BANK_REFUND_REQUEST(HttpStatus.BAD_REQUEST, "고객 계좌가 입금이 되지 않는 상태입니다."),
    NOT_MATCHES_REFUNDABLE_AMOUNT(HttpStatus.BAD_REQUEST, "잔액 결과가 일치하지 않습니다."),

    NOT_SUPPORTED_MONTHLY_INSTALLMENT_PLAN_BELOW_AMOUNT(HttpStatus.BAD_REQUEST, "5만원 이하의 결제는 할부가 불가능해서 결제에 실패했습니다."),

    INVALID_VERSION_ORDER(HttpStatus.BAD_REQUEST, "새로운 버전은 최신 버전보다 높은 버전이어야 합니다."),

    INVALID_RECEIPT(HttpStatus.BAD_REQUEST, "유효하지 않은 영수증입니다"),
    PRODUCT_MISMATCH(HttpStatus.BAD_REQUEST, "상품 정보가 일치하지 않습니다"),

    /**
     * 401 UNAUTHORIZED EXCEPTION
     */
    TOKEN_TIME_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    VERIFY_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 인증입니다. 재발송 해주세요."),
    REFRESH_TOKEN_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다. 재로그인해주세요."),
    EXPIRED_APPLE_IDENTITY_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 아이덴티티 토큰입니다."),
    UN_AUTHORIZED(HttpStatus.UNAUTHORIZED, "토큰 인증을 안한 유저입니다."),
    UNAUTHORIZED_KEY(HttpStatus.UNAUTHORIZED, "인증되지 않은 시크릿 키 혹은 클라이언트 키 입니다."),
    INVALID_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_QR_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "유효하지 않은 QR토큰입니다."),
    INVALID_SEARCH_EMPTY_EXCEPTION(HttpStatus.UNAUTHORIZED, "검색어는 비어 있을 수 없습니다."),

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
    REJECT_ACCOUNT_PAYMENT(HttpStatus.FORBIDDEN, "잔액부족으로 결제에 실패했습니다."),
    REJECT_CARD_PAYMENT(HttpStatus.FORBIDDEN, "한도초과 혹은 잔액부족으로 결제에 실패했습니다."),
    REJECT_CARD_COMPANY(HttpStatus.FORBIDDEN, "결제 승인이 거절되었습니다."),
    FORBIDDEN_REQUEST(HttpStatus.FORBIDDEN, "허용되지 않은 요청입니다."),
    REJECT_TOSSPAY_INVALID_ACCOUNT(HttpStatus.FORBIDDEN, "선택하신 출금 계좌가 출금이체 등록이 되어 있지 않아요. 계좌를 다시 등록해 주세요."),
    EXCEED_MAX_AUTH_COUNT(HttpStatus.FORBIDDEN, "최대 인증 횟수를 초과했습니다. 카드사로 문의해주세요."),
    EXCEED_MAX_ONE_DAY_AMOUNT(HttpStatus.FORBIDDEN, "일일 한도를 초과했습니다."),
    NOT_AVAILABLE_BANK(HttpStatus.FORBIDDEN, "은행 서비스 시간이 아닙니다."),
    INVALID_PASSWORD(HttpStatus.FORBIDDEN, "결제 비밀번호가 일치하지 않습니다."),
    INCORRECT_BASIC_AUTH_FORMAT(HttpStatus.FORBIDDEN, "잘못된 요청입니다. ':' 를 포함해 인코딩해주세요."),
    FDS_ERROR(HttpStatus.FORBIDDEN, "[토스페이먼츠] 위험거래가 감지되어 결제가 제한됩니다. 발송된 문자에 포함된 링크를 통해 본인인증 후 결제가 가능합니다. (고객센터: 1644-8051)"),

    NOT_CANCELABLE_AMOUNT(HttpStatus.FORBIDDEN, "취소 할 수 없는 금액 입니다."),
    FORBIDDEN_CONSECUTIVE_REQUEST(HttpStatus.FORBIDDEN, "반복적인 요청은 허용되지 않습니다. 잠시 후 다시 시도해주세요."),
    NOT_CANCELABLE_PAYMENT(HttpStatus.FORBIDDEN, "취소 할 수 없는 결제 입니다."),
    EXCEED_MAX_REFUND_DUE(HttpStatus.FORBIDDEN, "환불 가능한 기간이 지났습니다."),
    NOT_ALLOWED_PARTIAL_REFUND_WAITING_DEPOSIT(HttpStatus.FORBIDDEN, "입금 대기중인 결제는 부분 환불이 불가합니다."),
    NOT_ALLOWED_PARTIAL_REFUND(HttpStatus.FORBIDDEN, "에스크로 주문, 현금 카드 결제일 때는 부분 환불이 불가합니다. 이외 다른 결제 수단에서 부분 취소가 되지 않을 때는 토스페이먼츠에 문의해 주세요."),
    NOT_CANCELABLE_PAYMENT_FOR_DORMANT_USER(HttpStatus.FORBIDDEN, "휴면 처리된 회원의 결제는 취소할 수 없습니다."),

    PRODUCT_NOT_AVAILABLE(HttpStatus.FORBIDDEN, "구매할 수 없는 상품입니다"),

    /**
     * 422 UNPROCESSABLE_ENTITY
     */
    UNPROCESSABLE_ENTITY_DELETE_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY, "서버에서 요청을 이해해 삭제하려는 도중 문제가 생겼습니다."),
    UNPROCESSABLE_INSTRUCTOR_DELETE_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY, "강사는 계약 양도 이후에 삭제할 수 있습니다."),
    UNPROCESSABLE_KAKAO_SERVER_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY, "카카오서버와 통신 중 오류가 발생했습니다."),
    UNPROCESSABLE_APPLE_SERVER_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY, "카카오서버와 통신 중 오류가 발생했습니다."),
    UNPROCESSABLE_JWT_CREATE_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY, "서버에서 KOLLUS JWT토큰을 생성 중 오류가 발생했습니다."),

    APPLE_VERIFICATION_FAILED(HttpStatus.UNPROCESSABLE_ENTITY, "Apple 영수증 검증에 실패했습니다"),

    /**
     * 409 UNPROCESSABLE_ENTITY
     */
    VERSION_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 버전입니다."),
    DUPLICATE_PURCHASE(HttpStatus.CONFLICT, "이미 처리된 구매입니다"),

    /**
     * 500 INTERNAL_SERVER_ERROR
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러가 발생했습니다"),
    INVALID_ENCRYPT_COMMUNICATION(HttpStatus.INTERNAL_SERVER_ERROR, "ios 통신 증명 과정 중 문제가 발생했습니다."),
    CREATE_PUBLIC_KEY_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "publickey 생성 과정 중 문제가 발생했습니다."),
    FAILED_PARSING_TOSSPAY_ERROR_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "토스 결제 승인 API의 에러코드 파싱에 문제가 발생했습니다."),

    FAILED_REFUND_PROCESS(HttpStatus.INTERNAL_SERVER_ERROR, "은행 응답시간 지연이나 일시적인 오류로 환불요청에 실패했습니다."),
    FAILED_METHOD_HANDLING_CANCEL(HttpStatus.INTERNAL_SERVER_ERROR, "취소 중 결제 시 사용한 결제 수단 처리과정에서 일시적인 오류가 발생했습니다."),
    FAILED_PARTIAL_REFUND(HttpStatus.INTERNAL_SERVER_ERROR, "은행 점검, 해약 계좌 등의 사유로 부분 환불이 실패했습니다."),
    FAILED_CLOSED_ACCOUNT_REFUND(HttpStatus.INTERNAL_SERVER_ERROR, "해약된 계좌로 인해 환불이 실패했습니다."),
    COMMON_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    PAYMENT_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "결제는 성공적으로 처리되었으나, 시스템에 주문 정보를 저장하는 과정에서 오류가 발생했습니다."),

    PAYMENT_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "결제 처리 중 오류가 발생했습니다"),

    SUGANG_STATUS_UPDATE(HttpStatus.INTERNAL_SERVER_ERROR, "수강 결제 상태를 변경하지 못했습니다."),
    CONCURRENT_PURCHASE_ATTEMPT(HttpStatus.INTERNAL_SERVER_ERROR, "Product is currently being processed by another user")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getErrorCode() {
        return httpStatus.value();
    }

    private static final Map<String, Error> tossErrorMap = new HashMap<>();

    static {
        tossErrorMap.put("ALREADY_PROCESSED_PAYMENT", ALREADY_PROCESSED_PAYMENT);
        tossErrorMap.put("PROVIDER_ERROR", PROVIDER_ERROR);
        tossErrorMap.put("EXCEED_MAX_CARD_INSTALLMENT_PLAN", EXCEED_MAX_CARD_INSTALLMENT_PLAN);
        tossErrorMap.put("INVALID_REQUEST", INVALID_REQUEST);
        tossErrorMap.put("NOT_ALLOWED_POINT_USE", NOT_ALLOWED_POINT_USE);
        tossErrorMap.put("INVALID_API_KEY", INVALID_API_KEY);
        tossErrorMap.put("INVALID_REJECT_CARD", INVALID_REJECT_CARD);
        tossErrorMap.put("BELOW_MINIMUM_AMOUNT", BELOW_MINIMUM_AMOUNT);
        tossErrorMap.put("INVALID_CARD_EXPIRATION", INVALID_CARD_EXPIRATION);
        tossErrorMap.put("INVALID_STOPPED_CARD", INVALID_STOPPED_CARD);
        tossErrorMap.put("EXCEED_MAX_DAILY_PAYMENT_COUNT", EXCEED_MAX_DAILY_PAYMENT_COUNT);
        tossErrorMap.put("NOT_SUPPORTED_INSTALLMENT_PLAN_CARD_OR_MERCHANT", NOT_SUPPORTED_INSTALLMENT_PLAN_CARD_OR_MERCHANT);
        tossErrorMap.put("INVALID_CARD_INSTALLMENT_PLAN", INVALID_CARD_INSTALLMENT_PLAN);
        tossErrorMap.put("NOT_SUPPORTED_MONTHLY_INSTALLMENT_PLAN", NOT_SUPPORTED_MONTHLY_INSTALLMENT_PLAN);
        tossErrorMap.put("EXCEED_MAX_PAYMENT_AMOUNT", EXCEED_MAX_PAYMENT_AMOUNT);
        tossErrorMap.put("NOT_FOUND_TERMINAL_ID", NOT_FOUND_TERMINAL_ID);
        tossErrorMap.put("INVALID_AUTHORIZE_AUTH", INVALID_AUTHORIZE_AUTH);
        tossErrorMap.put("INVALID_CARD_LOST_OR_STOLEN", INVALID_CARD_LOST_OR_STOLEN);
        tossErrorMap.put("RESTRICTED_TRANSFER_ACCOUNT", RESTRICTED_TRANSFER_ACCOUNT);
        tossErrorMap.put("INVALID_CARD_NUMBER", INVALID_CARD_NUMBER);
        tossErrorMap.put("INVALID_UNREGISTERED_SUBMALL", INVALID_UNREGISTERED_SUBMALL);
        tossErrorMap.put("NOT_REGISTERED_BUSINESS", NOT_REGISTERED_BUSINESS);
        tossErrorMap.put("EXCEED_MAX_ONE_DAY_WITHDRAW_AMOUNT", EXCEED_MAX_ONE_DAY_WITHDRAW_AMOUNT);
        tossErrorMap.put("EXCEED_MAX_ONE_TIME_WITHDRAW_AMOUNT", EXCEED_MAX_ONE_TIME_WITHDRAW_AMOUNT);
        tossErrorMap.put("CARD_PROCESSING_ERROR", CARD_PROCESSING_ERROR);
        tossErrorMap.put("EXCEED_MAX_AMOUNT", EXCEED_MAX_AMOUNT);
        tossErrorMap.put("INVALID_ACCOUNT_INFO_RE_REGISTER", INVALID_ACCOUNT_INFO_RE_REGISTER);
        tossErrorMap.put("NOT_AVAILABLE_PAYMENT", NOT_AVAILABLE_PAYMENT);
        tossErrorMap.put("UNAPPROVED_ORDER_ID", UNAPPROVED_ORDER_ID);
        tossErrorMap.put("EXCEED_MAX_MONTHLY_PAYMENT_AMOUNT", EXCEED_MAX_MONTHLY_PAYMENT_AMOUNT);
        tossErrorMap.put("UNAUTHORIZED_KEY", UNAUTHORIZED_KEY);
        tossErrorMap.put("REJECT_ACCOUNT_PAYMENT", REJECT_ACCOUNT_PAYMENT);
        tossErrorMap.put("REJECT_CARD_PAYMENT", REJECT_CARD_PAYMENT);
        tossErrorMap.put("REJECT_CARD_COMPANY", REJECT_CARD_COMPANY);
        tossErrorMap.put("FORBIDDEN_REQUEST", FORBIDDEN_REQUEST);
        tossErrorMap.put("REJECT_TOSSPAY_INVALID_ACCOUNT", REJECT_TOSSPAY_INVALID_ACCOUNT);
        tossErrorMap.put("EXCEED_MAX_AUTH_COUNT", EXCEED_MAX_AUTH_COUNT);
        tossErrorMap.put("EXCEED_MAX_ONE_DAY_AMOUNT", EXCEED_MAX_ONE_DAY_AMOUNT);
        tossErrorMap.put("NOT_AVAILABLE_BANK", NOT_AVAILABLE_BANK);
        tossErrorMap.put("INVALID_PASSWORD", INVALID_PASSWORD);
        tossErrorMap.put("INCORRECT_BASIC_AUTH_FORMAT", INCORRECT_BASIC_AUTH_FORMAT);
        tossErrorMap.put("FDS_ERROR", FDS_ERROR);
        tossErrorMap.put("NOT_FOUND_PAYMENT", NOT_FOUND_PAYMENT);
        tossErrorMap.put("NOT_FOUND_PAYMENT_SESSION", NOT_FOUND_PAYMENT_SESSION);
        tossErrorMap.put("FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING", FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING);
        tossErrorMap.put("FAILED_INTERNAL_SYSTEM_PROCESSING", FAILED_INTERNAL_SYSTEM_PROCESSING);
        tossErrorMap.put("UNKNOWN_PAYMENT_ERROR", UNKNOWN_PAYMENT_ERROR);

        tossErrorMap.put("NOT_SUPPORTED_MONTHLY_INSTALLMENT_PLAN_BELOW_AMOUNT", NOT_SUPPORTED_MONTHLY_INSTALLMENT_PLAN_BELOW_AMOUNT);
        tossErrorMap.put("NOT_FOUND", NOT_FOUND);

        tossErrorMap.put("ALREADY_CANCELED_PAYMENT", ALREADY_CANCELED_PAYMENT);
        tossErrorMap.put("INVALID_REFUND_ACCOUNT_INFO", INVALID_REFUND_ACCOUNT_INFO);
        tossErrorMap.put("EXCEED_CANCEL_AMOUNT_DISCOUNT_AMOUNT", EXCEED_CANCEL_AMOUNT_DISCOUNT_AMOUNT);
        tossErrorMap.put("INVALID_REFUND_ACCOUNT_NUMBER", INVALID_REFUND_ACCOUNT_NUMBER);
        tossErrorMap.put("NOT_MATCHES_REFUNDABLE_AMOUNT", NOT_MATCHES_REFUNDABLE_AMOUNT);
        tossErrorMap.put("REFUND_REJECTED", REFUND_REJECTED);
        tossErrorMap.put("ALREADY_REFUND_PAYMENT", ALREADY_REFUND_PAYMENT);
        tossErrorMap.put("FORBIDDEN_BANK_REFUND_REQUEST", FORBIDDEN_BANK_REFUND_REQUEST);
        tossErrorMap.put("NOT_CANCELABLE_AMOUNT", NOT_CANCELABLE_AMOUNT);
        tossErrorMap.put("FORBIDDEN_CONSECUTIVE_REQUEST", FORBIDDEN_CONSECUTIVE_REQUEST);
        tossErrorMap.put("NOT_CANCELABLE_PAYMENT", NOT_CANCELABLE_PAYMENT);
        tossErrorMap.put("EXCEED_MAX_REFUND_DUE", EXCEED_MAX_REFUND_DUE);
        tossErrorMap.put("NOT_ALLOWED_PARTIAL_REFUND_WAITING_DEPOSIT", NOT_ALLOWED_PARTIAL_REFUND_WAITING_DEPOSIT);
        tossErrorMap.put("NOT_ALLOWED_PARTIAL_REFUND", NOT_ALLOWED_PARTIAL_REFUND);
        tossErrorMap.put("NOT_CANCELABLE_PAYMENT_FOR_DORMANT_USER", NOT_CANCELABLE_PAYMENT_FOR_DORMANT_USER);
        tossErrorMap.put("FAILED_REFUND_PROCESS", FAILED_REFUND_PROCESS);
        tossErrorMap.put("FAILED_METHOD_HANDLING_CANCEL", FAILED_METHOD_HANDLING_CANCEL);
        tossErrorMap.put("FAILED_PARTIAL_REFUND", FAILED_PARTIAL_REFUND);
        tossErrorMap.put("COMMON_ERROR", COMMON_ERROR);
        tossErrorMap.put("FAILED_CLOSED_ACCOUNT_REFUND", FAILED_CLOSED_ACCOUNT_REFUND);
    }

    public static Error fromTossPaymentCode(String tossCode) {
        return tossErrorMap.getOrDefault(tossCode, UNKNOWN_PAYMENT_ERROR);
    }
}

