package com.app.bdink.payment.service;

import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.exception.NotFoundMemberException;
import com.app.bdink.member.repository.MemberRepository;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.payment.controller.dto.PaymentResponse;
import com.app.bdink.payment.entity.EssentialPayment;
import com.app.bdink.payment.repository.EssentialPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionalPaymentService {

    private final EssentialPaymentRepository essentialPaymentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public EssentialPayment savePaymentTransactional(Long memberId, PaymentResponse response) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundMemberException(Error.NOT_FOUND_USER_EXCEPTION, "해당 멤버를 찾지 못했습니다.")
        );
        EssentialPayment payment = EssentialPayment.from(member, response);
        return essentialPaymentRepository.save(payment);
    }
}
