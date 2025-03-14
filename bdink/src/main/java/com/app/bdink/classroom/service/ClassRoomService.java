package com.app.bdink.classroom.service;

import com.app.bdink.classroom.controller.dto.request.ClassRoomDto;
import com.app.bdink.classroom.controller.dto.response.ClassRoomResponse;
import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.entity.Instructor;
import com.app.bdink.classroom.repository.ClassRoomRepository;
import com.app.bdink.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public String createClassRoom(final Instructor instructor,
                                  final String thumbnailKey,
                                  final String mediaKey,
                                  final ClassRoomDto classRoomDto) {

        ClassRoom classRoom = ClassRoom.builder()
                .title(classRoomDto.title())
                .introduction(classRoomDto.introduction())
                .instructor(instructor)
                .thumbnail(thumbnailKey)
                .introLink(mediaKey)
                .priceDetail(classRoomDto.priceDto().toPriceDetail())
                .build();
        Long id = classRoomRepository.save(classRoom).getId();
        return String.valueOf(id);
    }

    @Transactional
    public ClassRoomResponse updateClassRoomInfo(final ClassRoom classRoom,
                                                 final String thumbnailKey,
                                                 final String videoKey,
                                                 final ClassRoomDto classRoomDto){
        classRoom.modifyClassRoom(classRoomDto, thumbnailKey, videoKey);
        return new ClassRoomResponse(
                classRoom.getId(),
                classRoom.getTitle(),
                classRoom.getIntroduction(),
                thumbnailKey,
                videoKey,
                classRoom.getPriceDetail()
        );
    }

    @Transactional
    public void updateClassRoomCdn(Long id, String assetId){
        ClassRoom classRoom = findById(id);
        classRoom.updateCDNLink(assetId);
    }

    @Transactional
    public void deleteClassRoom(final ClassRoom classRoom){
        classRoomRepository.delete(classRoom);
    }

    @Transactional(readOnly = true)
    public List<ClassRoomResponse> getAllClassRoom() {
        List<ClassRoom> classRooms = classRoomRepository.findAll();
        return classRooms.stream()
                .map(ClassRoomResponse::from)
                .collect(Collectors.toList());
    }

}
