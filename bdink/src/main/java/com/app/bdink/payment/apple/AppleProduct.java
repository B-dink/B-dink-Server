package com.app.bdink.payment.apple;

import com.app.bdink.common.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class AppleProduct extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String productId;

    private Boolean canPurchase;
}
