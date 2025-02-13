package com.app.bdink.classroom.controller.dto.request;

import com.app.bdink.classroom.domain.PriceDetail;

public record PriceDto(
        int originPrice,
        float discountRate
) {
    public PriceDetail toPriceDetail(){
        return new PriceDetail(originPrice, discountRate);
    }
}
