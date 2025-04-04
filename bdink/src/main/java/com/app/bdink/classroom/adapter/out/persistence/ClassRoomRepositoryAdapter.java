package com.app.bdink.classroom.adapter.out.persistence;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.domain.ClassRoom;
import com.app.bdink.price.domain.PriceDetail;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.classroom.mapper.ClassRoomMapper;
import com.app.bdink.instructor.mapper.InstructorMapper;
import com.app.bdink.classroom.port.out.CreateClassRoomPort;
import com.app.bdink.classroom.repository.ClassRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClassRoomRepositoryAdapter implements CreateClassRoomPort {

    private final ClassRoomRepository classRoomRepository;
    private final ClassRoomMapper classRoomMapper;
    private final InstructorMapper instructorMapper;
    @Override //TODO: member 도메인으로 바꾸기.
    public ClassRoom createClassRoom(ClassRoom classRoom, PriceDetail priceDetail, Instructor instructor) { //도메인을 받고 디비에 저장.
        ClassRoomEntity classRoomEntity = classRoomMapper.toEntity(classRoom, instructor, priceDetail);
        ClassRoomEntity saved = classRoomRepository.save(classRoomEntity);
        return classRoomMapper.toDomain(saved);
    }
}
