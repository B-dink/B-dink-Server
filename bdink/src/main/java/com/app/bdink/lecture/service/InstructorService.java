package com.app.bdink.lecture.service;

import com.app.bdink.classroom.entity.Instructor;
import com.app.bdink.lecture.controller.dto.InstructorDto;
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
                ()-> new IllegalStateException("해당 강사를 찾지 못했습니다.")
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
}
