package com.app.bdink.payment.transactional;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.repository.ClassRoomRepository;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.exception.model.NotFoundException;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.exception.NotFoundMemberException;
import com.app.bdink.member.repository.MemberRepository;
import com.app.bdink.payment.toss.controller.dto.PaymentResponse;
import com.app.bdink.payment.toss.entity.EssentialPayment;
import com.app.bdink.payment.toss.repository.EssentialPaymentRepository;
import com.app.bdink.sugang.controller.dto.SugangStatus;
import com.app.bdink.sugang.entity.Sugang;
import com.app.bdink.sugang.repository.SugangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentTransactionalServiceImpl implements PaymentTransactionalService {

    private final EssentialPaymentRepository essentialPaymentRepository;
    private final MemberRepository memberRepository;
    private final SugangRepository sugangRepository;
    private final ClassRoomRepository classRoomRepository;

    @Transactional
    public EssentialPayment savePaymentTransactional(Long memberId, PaymentResponse response) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundMemberException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
        );
        EssentialPayment payment = EssentialPayment.from(member, response);
        return essentialPaymentRepository.save(payment);
    }

    @Transactional
    public void updateSugangStatus(Long memberId, Long classRoomId, SugangStatus sugangStatus) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundMemberException(Error.NOT_FOUND_USER_EXCEPTION, Error.NOT_FOUND_USER_EXCEPTION.getMessage())
        );
        ClassRoomEntity classRoomEntity = classRoomRepository.findById(classRoomId).orElseThrow(
                () -> new NotFoundException(Error.NOT_FOUND_CLASSROOM, Error.NOT_FOUND_CLASSROOM.getMessage())
        );
        Sugang sugang = sugangRepository.findByMemberAndClassRoomEntity(member, classRoomEntity).orElseThrow(
                () -> new NotFoundException(Error.NOT_FOUND_SUGANG, Error.NOT_FOUND_SUGANG.getMessage())
        );
        sugang.updateSugangStatus(sugangStatus);
    }
}
