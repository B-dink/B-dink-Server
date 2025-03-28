package com.app.bdink.enrollment.domain;

public enum PaymentStatus {
    COMPLETED, //결제 성공
    FAILED, //결제 실패
    CANCELED, // 결제 취소
    IN_PROGRESS //진행중
}
