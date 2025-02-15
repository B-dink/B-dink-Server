package com.app.bdink.lecture.service;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.lecture.controller.dto.LectureDto;
import com.app.bdink.lecture.controller.dto.response.LectureInfo;
import com.app.bdink.lecture.entity.Chapter;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.lecture.repository.LectureRepository;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;

    private final MemberService memberService;
    private final ClassRoomService classRoomService;


    public Lecture findById(Long id){
        return lectureRepository.findById(id).orElseThrow(
                ()-> new IllegalStateException("해당 강좌를 찾지 못했습니다.")
        );
    }

    //chapter에 강좌 추가
    @Transactional
    public String createLecture(final ClassRoom classRoom, final Chapter chapter, final LectureDto lectureDto){

        Lecture lecture = lectureRepository.save(
                Lecture.builder()
                        .classRoom(chapter.getClassRoom())
                        .chapter(chapter) //강좌를 만들때 챕터는 무조건 있어야한다.
                        .title(lectureDto.title())
                        .time(lectureDto.convertToLocalTime())
                        .build());

        chapter.increaseLectureCount();
        chapter.addLectures(lecture);

        return String.valueOf(lecture.getId());
    }

    @Transactional(readOnly = true)
    public LectureInfo getLectureInfo(Long id){

        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalStateException("존재하지 않는 강의입니다.")
        );
        return LectureInfo.from(lecture);
    }

    @Transactional
    public void deleteLecture(long id){
        Lecture lecture = findById(id);
        lectureRepository.delete(lecture);
    }
}
