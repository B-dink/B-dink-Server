package com.app.bdink.price.mapper;

import com.app.bdink.price.controller.dto.PriceDto;
import com.app.bdink.price.domain.PriceDetail;
import com.app.bdink.classroom.service.command.CreateClassRoomCommand;
import org.springframework.stereotype.Component;

@Component
public class PriceDetailMapper {

    public PriceDetail commandToDomain(CreateClassRoomCommand command){
        PriceDto priceDto = command.classRoomDto().priceDto();
        return new PriceDetail(
                priceDto.originPrice(),
                priceDto.discountRate());
    }
}
