package com.app.bdink.payment.transactional;

import com.app.bdink.payment.toss.controller.dto.PaymentResponse;
import com.app.bdink.payment.toss.entity.EssentialPayment;
import com.app.bdink.sugang.controller.dto.SugangStatus;

public interface PaymentTransactionalService {

    /**
     * 결제 정보를 트랜잭션 내에서 저장합니다.
     *
     * @param memberId 회원 ID
     * @param response 결제 응답 정보
     * @return 저장된 EssentialPayment 엔티티
     */
    EssentialPayment savePaymentTransactional(Long memberId, PaymentResponse response);

    /**
     * 수강 상태를 업데이트합니다.
     *
     * @param memberId 회원 ID
     * @param classRoomId 강의실 ID
     * @param sugangStatus 변경할 수강 상태
     */
    void updateSugangStatus(Long memberId, Long classRoomId, SugangStatus sugangStatus);
}