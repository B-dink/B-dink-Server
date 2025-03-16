package com.app.bdink.classroom.util;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.entity.Instructor;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.lecture.service.LectureService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class InstructorUtilService {

    //TODO: 나중에 여기가 레포지터리로 변할듯.
    private final MemberService memberService;
    private final ClassRoomService classRoomService;
    private final LectureService lectureService;

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
        ClassRoom classRoom = classRoomService.findById(classRoomId);

        if (instructor.getId().equals(classRoom.getInstructor().getId())){
            return false;
        }

        return true;
    }

    @Transactional(readOnly = true)
    public boolean validateLectureOwner(Principal principal, Long lectureId){
        Instructor instructor = getInstructor(principal);
        ClassRoom classRoom = lectureService.findById(lectureId).getClassRoom();

        if (instructor.getId().equals(classRoom.getInstructor().getId())){
            return false;
        }

        return true;
    }






}
