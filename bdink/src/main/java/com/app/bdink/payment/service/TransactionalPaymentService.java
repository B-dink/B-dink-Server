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

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class TransactionalPaymentService {

    private final EssentialPaymentRepository essentialPaymentRepository;
    private final MemberUtilService memberUtilService;
    private final MemberRepository memberRepository;

    @Transactional
    public EssentialPayment savePaymentTransactional(Principal principal, PaymentResponse response) {
        Member member = findMemberByPrinciple(principal);
        EssentialPayment payment = EssentialPayment.from(member, response);
        return essentialPaymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public Member findMemberByPrinciple(Principal principal) {
        Long memberId = memberUtilService.getMemberId(principal);
        return memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundMemberException(Error.NOT_FOUND_USER_EXCEPTION, "해당 멤버를 찾지 못했습니다.")
        );
    }
}
