package com.app.bdink.subscription.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "subscription_plan")
public class TrainerMembershipPlan extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer billingCycleMonths;

    @Column(nullable = false)
    private Integer price;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Boolean active;

    @Builder
    public TrainerMembershipPlan(String name, Integer billingCycleMonths, Integer price, String description, Boolean active) {
        this.name = name;
        this.billingCycleMonths = billingCycleMonths;
        this.price = price;
        this.description = description;
        this.active = active == null ? Boolean.TRUE : active;
    }

    public void update(String name, Integer billingCycleMonths, Integer price, String description, Boolean active) {
        if (name != null) {
            this.name = name;
        }
        if (billingCycleMonths != null) {
            this.billingCycleMonths = billingCycleMonths;
        }
        if (price != null) {
            this.price = price;
        }
        if (description != null) {
            this.description = description;
        }
        if (active != null) {
            this.active = active;
        }
    }
}
