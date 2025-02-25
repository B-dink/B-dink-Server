package com.app.bdink.classroom.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class PriceDetailTest {

    @ParameterizedTest
    @CsvSource({
            "10000,         0.3,        7000",
            "10000,         0.2,        8000",
            "0,             0.2,        0",
    })
    @DisplayName("할인율을 주면 할인된 가격을 리턴한다.")
    public void returnDiscountedPrice(int discountPrice, float discountRate, int result){
        //given
        PriceDetail priceDetail = new PriceDetail(discountPrice, discountRate);

        //when
        int expected = priceDetail.getDiscountPrice();

        //then
        Assertions.assertEquals(expected, result);
    }
}
