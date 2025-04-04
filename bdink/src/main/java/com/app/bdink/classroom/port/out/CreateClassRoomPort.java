package com.app.bdink.classroom.port.out;

import com.app.bdink.classroom.domain.ClassRoom;
import com.app.bdink.price.domain.PriceDetail;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;

public interface CreateClassRoomPort { //TODO: Member 도메인으로 바꾸기.

    ClassRoom createClassRoom(ClassRoom classRoom, PriceDetail priceDetail, Instructor instructor);
}
