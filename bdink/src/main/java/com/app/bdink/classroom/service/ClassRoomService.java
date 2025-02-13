package com.app.bdink.classroom.service;

import com.app.bdink.classroom.controller.dto.request.ClassRoomDto;
import com.app.bdink.classroom.controller.dto.response.ClassRoomResponse;
import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.repository.ClassRoomRepository;
import com.app.bdink.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassRoomService {

    private final ClassRoomRepository classRoomRepository;

    public ClassRoom findById(Long id){
        return classRoomRepository.findById(id).orElseThrow(
                ()-> new IllegalStateException("해당 클래스를 찾지 못했습니다.")
        );
    }

    @Transactional(readOnly = true)
    public ClassRoomResponse getClassRoomInfo(final Long id) {
        ClassRoom classRoom = findById(id);
        return ClassRoomResponse.from(classRoom);
    }

    @Transactional
    public String createClassRoom(final ClassRoomDto classRoomDto) {
        Long id = classRoomRepository.save(
                    classRoomDto.toEntity()
                )
                .getId();
        return String.valueOf(id);
    }

    @Transactional
    public ClassRoomResponse updateClassRoomInfo(final Long id, final ClassRoomDto classRoomDto){
        ClassRoom classRoom = findById(id);
        classRoom.modifyClassRoom(classRoomDto);
        return new ClassRoomResponse(
                classRoom.getId(),
                classRoom.getTitle(),
                classRoom.getIntroduction(),
                classRoom.getPriceDetail()
        );
    }
}
