package com.app.bdink.classroom.service;

import com.app.bdink.classroom.controller.dto.ClassRoomDto;
import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.repository.ClassRoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassRoomService {

    private final ClassRoomRepository classRoomRepository;

    @Transactional(readOnly = true)
    public ClassRoom getClassRoom(final Long id){
        Optional<ClassRoom> classRoom = classRoomRepository.findById(id);
        if (classRoom.isEmpty()){
            throw new IllegalStateException("해당 클래스 룸이 존재하지 않습니다.");
        }
        return classRoom.get();
    }

    @Transactional
    public String createClassRoom(final ClassRoomDto classRoomDto){
        Long id = classRoomRepository.save(
                classRoomDto.toEntity())
                .getId();
        return String.valueOf(id);
    }
}
