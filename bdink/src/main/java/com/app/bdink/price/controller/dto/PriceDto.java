package com.app.bdink.price.controller.dto;

import com.app.bdink.price.domain.PriceDetail;

public record PriceDto(
        int originPrice,
        float discountRate
) {
    public PriceDetail toPriceDetail(){
        return new PriceDetail(originPrice, discountRate);
    }
}
