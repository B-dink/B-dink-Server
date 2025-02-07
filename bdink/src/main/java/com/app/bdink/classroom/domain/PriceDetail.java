package com.app.bdink.classroom.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class PriceDetail {

    private int originPrice;

    private int discountPrice;

    private float discountRate;

    public PriceDetail(int originPrice, float discountRate) {
        this.originPrice = originPrice;
        this.discountRate = discountRate;
        this.discountPrice = (int) (originPrice * discountRate);
    }

}
