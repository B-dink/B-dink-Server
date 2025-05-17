package com.app.bdink.mypage.service;

import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.instructor.repository.InstructorRepository;
import com.app.bdink.member.entity.Member;
import com.app.bdink.mypage.dto.response.MyPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPageService {

    private final InstructorRepository instructorRepository;

    public MyPageResponse getMemberInfo(Member member) {
        Boolean isInstructor = false;
        Optional<Instructor> instructor = instructorRepository.findByMemberId(member.getId());
        if (instructor.isPresent()) {
            isInstructor = true;
        }

        return MyPageResponse.of(member, isInstructor);
    }
}
