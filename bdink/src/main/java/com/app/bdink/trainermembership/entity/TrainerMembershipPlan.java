package com.app.bdink.trainermembership.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "trainer_membership_plan")
public class TrainerMembershipPlan extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer billingCycleMonths;

    // 원래 가격
    @Column(nullable = false)
    private Integer price;

    // 할인된 가격
    @Column
    private Integer discountPrice;

    // 월 가격
    @Column
    private Integer monthlyPrice;

    @Column(nullable = false)
    private TrainerMembershipTag tag;

    @Column(nullable = false)
    private Integer discountRate;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private Boolean isAnnualProduct;

    @Builder
    public TrainerMembershipPlan(String name, Integer billingCycleMonths, Integer price,
                                 Integer discountPrice, Integer monthlyPrice,
                                 TrainerMembershipTag tag, Integer discountRate,
                                 String description, Boolean active, Boolean isAnnualProduct) {
        this.name = name;
        this.billingCycleMonths = billingCycleMonths;
        this.price = price;
        this.discountPrice = discountPrice;
        this.monthlyPrice = monthlyPrice;
        this.tag = tag;
        this.discountRate = discountRate;
        this.description = description;
        this.active = active == null ? Boolean.TRUE : active;
        this.isAnnualProduct = isAnnualProduct == null ? Boolean.FALSE : isAnnualProduct;
    }

    public void update(String name, Integer billingCycleMonths, Integer price,
                       Integer discountPrice, Integer monthlyPrice,
                       TrainerMembershipTag tag, Integer discountRate,
                       String description, Boolean active, Boolean isAnnualProduct) {
        if (name != null) {
            this.name = name;
        }
        if (billingCycleMonths != null) {
            this.billingCycleMonths = billingCycleMonths;
        }
        if (price != null) {
            this.price = price;
        }
        if (discountPrice != null) {
            this.discountPrice = discountPrice;
        }
        if (monthlyPrice != null) {
            this.monthlyPrice = monthlyPrice;
        }
        if (tag != null) {
            this.tag = tag;
        }
        if (discountRate != null) {
            this.discountRate = discountRate;
        }
        if (description != null) {
            this.description = description;
        }
        if (active != null) {
            this.active = active;
        }
        if (isAnnualProduct != null) {
            this.isAnnualProduct = isAnnualProduct;
        }
    }
}
