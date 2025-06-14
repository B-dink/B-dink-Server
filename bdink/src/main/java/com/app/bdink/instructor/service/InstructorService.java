package com.app.bdink.instructor.service;

import com.app.bdink.classroom.adapter.in.controller.dto.response.AllClassRoomResponse;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.classroom.repository.ClassRoomRepository;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.instructor.adapter.in.controller.dto.response.InstructorAllInfoDto;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.instructor.adapter.in.controller.dto.InstructorDto;
import com.app.bdink.instructor.adapter.in.controller.dto.request.UpdateInstructorDto;
import com.app.bdink.instructor.adapter.in.controller.dto.response.InstructorInfoDto;
import com.app.bdink.instructor.repository.InstructorRepository;
import com.app.bdink.lecture.repository.LectureRepository;
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
    public String createInstructor(final Member member, InstructorDto instructorDto, String marketingImage){
        Long id = instructorRepository.save(
                Instructor.builder()
                        .member(member)
                        .career(instructorDto.toCareer())
                        .marketingText(instructorDto.marketingText())
                        .marketingImage(marketingImage)
                        .marketingSites(instructorDto.marketingSites())
                        .build()
        ).getId();
        return id.toString();
    }
    
    @Transactional(readOnly = true)
    public List<InstructorAllInfoDto> getAllInstructorInfo() {
        // 강사의 모든 정보를 담고있는 api처리를 위한 비즈니스로직
        List<Instructor> instructors = instructorRepository.findAll();

        return instructors.stream()
                .map(instructor -> {
                    int clasRoomCount = classRoomRepository.countByInstructor(instructor);
                    return InstructorAllInfoDto.from(instructor, clasRoomCount);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public InstructorInfoDto getInfo(final Member member){
        return InstructorInfoDto.from(member);
    }

    @Transactional
    public InstructorInfoDto modifyInstructorInfo(final Member member, final UpdateInstructorDto instructorDto, String marketingImage){
        Instructor instructor = member.getInstructor();

        instructor.modify(
                Career.valueOf(instructorDto.career()),
                marketingImage,
                instructorDto.marketingSites(),
                instructorDto.marketingText()
        );
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
    public void deleteInstructor(final Instructor instructor, List<ClassRoomEntity> classRoomList){
        classRoomList.stream().map(ClassRoomEntity::softDeleteInstructor);
        instructor.softDelete();
    }
}
