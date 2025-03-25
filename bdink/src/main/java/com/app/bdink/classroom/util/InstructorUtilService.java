package com.app.bdink.classroom.util;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.adapter.out.persistence.entity.Instructor;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.lecture.service.LectureService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.qna.entity.Answer;
import com.app.bdink.qna.service.AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstructorUtilService {

    //TODO: 나중에 여기가 레포지터리로 변할듯.
    private final MemberService memberService;
    private final ClassRoomService classRoomService;
    private final LectureService lectureService;
    private final AnswerService answerService;

    @Transactional(readOnly = true)
    public Instructor getInstructor(Principal principal){
        Long memberId = Long.parseLong(principal.getName());
        Member member = memberService.findById(memberId);
        Instructor instructor = member.getInstructor();

        if (instructor == null){
            throw new CustomException(Error.NO_INSTRUCTOR, Error.NO_INSTRUCTOR.getMessage());
        }

        return instructor;
    }

    @Transactional(readOnly = true)
    public boolean validateClassRoomOwner(Principal principal, Long classRoomId){
        Instructor instructor = getInstructor(principal);
        ClassRoomEntity classRoomEntity = classRoomService.findById(classRoomId);

        return instructor.getId().equals(classRoomEntity.getInstructor().getId());
    }

    @Transactional(readOnly = true)
    public boolean validateLectureOwner(Principal principal, Long lectureId){
        Instructor instructor = getInstructor(principal);
        ClassRoomEntity classRoomEntity = lectureService.findById(lectureId).getClassRoom();

        return instructor.getId().equals(classRoomEntity.getInstructor().getId());
    }

    @Transactional(readOnly = true)
    public boolean validateAccessAnswer(Principal principal, final ClassRoomEntity classRoomEntity){
        Instructor instructor = getInstructor(principal);

        return instructor.getId().equals(classRoomEntity.getInstructor().getId());
    }

    @Transactional(readOnly = true)
    public boolean validateAccessAnswer(Principal principal, final Long id){
        Instructor instructor = getInstructor(principal);
        Answer answer = answerService.getById(id);

        return instructor.equals(
                answer.getQuestion()
                        .getClassRoom()
                        .getInstructor());
    }






}
