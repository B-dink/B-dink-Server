package com.app.bdink.coupon.entity;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @ManyToOne
    private ClassRoomEntity classRoom;

    // 정률 할인율 (1~100)
    @Column(nullable = false)
    private int discountRate;

    // 회원 1인당 최대 사용 가능 횟수
    @Column(nullable = false)
    private int maxUsagePerMember;

    @Builder
    public Coupon(String code, ClassRoomEntity classRoom, int discountRate, int maxUsagePerMember) {
        this.code = code;
        this.classRoom = classRoom;
        this.discountRate = discountRate;
        this.maxUsagePerMember = maxUsagePerMember;
    }
}
