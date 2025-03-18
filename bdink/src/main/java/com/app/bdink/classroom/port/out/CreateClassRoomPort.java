package com.app.bdink.classroom.port.out;

import com.app.bdink.classroom.domain.ClassRoom;
import com.app.bdink.classroom.domain.InstructorDomain;
import com.app.bdink.classroom.domain.PriceDetail;
import com.app.bdink.classroom.adapter.out.persistence.entity.Instructor;
import com.app.bdink.member.entity.Member;

public interface CreateClassRoomPort { //TODO: Member 도메인으로 바꾸기.

    ClassRoom createClassRoom(ClassRoom classRoom, PriceDetail priceDetail, Instructor instructor);
}
