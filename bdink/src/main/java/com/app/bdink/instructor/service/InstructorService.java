package com.app.bdink.instructor.service;

import com.app.bdink.chapter.domain.ChapterSummary;
import com.app.bdink.classroom.adapter.in.controller.dto.response.AllClassRoomResponse;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.classroom.repository.ClassRoomRepository;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.instructor.adapter.in.controller.dto.InstructorDto;
import com.app.bdink.instructor.adapter.in.controller.dto.request.UpdateInstructorDto;
import com.app.bdink.instructor.adapter.in.controller.dto.response.InstructorInfoDto;
import com.app.bdink.instructor.repository.InstructorRepository;
import com.app.bdink.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final ClassRoomRepository classRoomRepository;
    private final ClassRoomService classRoomService;

    @Transactional(readOnly = true)
    public Instructor findById(Long id){
        return instructorRepository.findById(id).orElseThrow(
                ()-> new CustomException(Error.NOT_FOUND_INSTRUCTOR, Error.NOT_FOUND_INSTRUCTOR.getMessage())
        );
    }

    @Transactional
    public String createInstructor(final Member member, InstructorDto instructorDto){
        Long id = instructorRepository.save(
                Instructor.builder()
                        .member(member)
                        .career(instructorDto.toCareer())
                        .build()
        ).getId();
        return id.toString();
    }

    @Transactional(readOnly = true)
    public InstructorInfoDto getInfo(final Member member){
        return InstructorInfoDto.from(member);
    }

    @Transactional
    public InstructorInfoDto modifyInstructorInfo(final Member member, final UpdateInstructorDto instructorDto){
        Instructor instructor = member.getInstructor();
        instructor.modify(Career.valueOf(instructorDto.career()));
        return InstructorInfoDto.from(member);
    }

    @Transactional(readOnly = true)
    public List<AllClassRoomResponse> getInstructorInfoByCareer(Career career) {
        List<ClassRoomEntity> classRoomEntities = classRoomRepository.findAllByInstructorCareer(career);
        return classRoomEntities.stream()
                .map(classRoom -> AllClassRoomResponse.from(classRoom, classRoomService.getChapterSummary(classRoom.getId())))
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteInstructor(final Member member, List<ClassRoomEntity> classRoomList){
        Instructor instructor = member.getInstructor();
        classRoomList.stream().map(ClassRoomEntity::softDeleteInstructor);
        instructor.softDelete();
    }
}
