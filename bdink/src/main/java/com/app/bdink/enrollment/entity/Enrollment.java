package com.app.bdink.enrollment.entity;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.domain.ClassRoom;
import com.app.bdink.enrollment.domain.PaymentStatus;
import com.app.bdink.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClassRoomEntity classRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public Enrollment(ClassRoomEntity classRoom, Member member, PaymentStatus paymentStatus) {
        this.classRoom = classRoom;
        this.member = member;
        this.paymentStatus = paymentStatus; //결제 엔티티 필요.
    }

    public boolean isSuccessPayment(){
        if (this.paymentStatus.equals(PaymentStatus.COMPLETED)){
            return true;
        }
        return false;
    }
}
