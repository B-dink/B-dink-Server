package com.app.bdink.lecture.service;

import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.lecture.controller.dto.LectureDto;
import com.app.bdink.lecture.controller.dto.response.LectureInfo;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.lecture.repository.LectureRepository;
import com.app.bdink.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;

    private final MemberService memberService;
    private final ClassRoomService classRoomService;

    @Transactional
    public String createLecture(final Long memberId, final Long classRoomId, final LectureDto lectureDto){

        Long id = lectureRepository.save(
                Lecture.builder()
                        .member(memberService.findById(memberId))
                        .classRoom(classRoomService.findById(classRoomId))
                        .title(lectureDto.title())
                        .time(lectureDto.convertToLocalTime())
                        .build()
        ).getId();
        return String.valueOf(id);
    }

    @Transactional(readOnly = true)
    public LectureInfo getLectureInfo(Long id){

        Lecture lecture = lectureRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 강의입니다.")
        );
        return LectureInfo.from(lecture);
    }
}
