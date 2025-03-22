package com.app.bdink.lecture.service;

import com.app.bdink.classroom.domain.Career;
import com.app.bdink.classroom.adapter.out.persistence.entity.Instructor;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.lecture.controller.dto.InstructorDto;
import com.app.bdink.lecture.controller.dto.request.UpdateInstructorDto;
import com.app.bdink.lecture.controller.dto.response.InstructorInfoDto;
import com.app.bdink.lecture.repository.InstructorRepository;
import com.app.bdink.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;
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

    @Transactional
    public void deleteInstructor(final Member member){
        Instructor instructor = member.getInstructor();
        instructor.softDelete();
    }
}
