package com.app.bdink.price.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class PriceDetail {

    private int originPrice;

    private int discountPrice;

    private float discountRate;

    public PriceDetail(int originPrice, float discountRate) {
        this.originPrice = originPrice;
        this.discountRate = discountRate;
        this.discountPrice = (int) (originPrice * (1- discountRate));
    }

}
