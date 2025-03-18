package com.app.bdink.classroom.mapper;

import com.app.bdink.classroom.adapter.in.controller.dto.request.PriceDto;
import com.app.bdink.classroom.domain.ClassRoom;
import com.app.bdink.classroom.domain.PriceDetail;
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
