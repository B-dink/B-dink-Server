package com.app.bdink.lecture.service;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.lecture.controller.dto.LectureDto;
import com.app.bdink.lecture.controller.dto.response.LectureInfo;
import com.app.bdink.chapter.entity.Chapter;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.lecture.repository.LectureRepository;
import com.app.bdink.member.entity.Member;
import com.app.bdink.sugang.repository.SugangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final SugangRepository sugangRepository;

    public Lecture findById(Long id){
        return lectureRepository.findById(id).orElseThrow(
                ()-> new CustomException(Error.NOT_FOUND_LECTURE, Error.NOT_FOUND_LECTURE.getMessage())
        );
    }

    //chapter에 강좌 추가
    @Transactional
    public String createLecture(final Chapter chapter,
                                final LectureDto lectureDto, final String uploadUrl){

        Lecture lecture = lectureRepository.save(
                Lecture.builder()
                        .classRoom(chapter.getClassRoom())
                        .chapter(chapter) //강좌를 만들때 챕터는 무조건 있어야한다.
                        .title(lectureDto.title())
                        .time(lectureDto.convertToLocalTime())
                        .mediaLink(uploadUrl)
                        .build());

        chapter.addLectures(lecture);

        return String.valueOf(lecture.getId());
    }

    @Transactional(readOnly = true)
    public LectureInfo getLectureInfo(Long id){

        Lecture lecture = findById(id);
        return LectureInfo.from(lecture);
    }

    @Transactional
    public void deleteLecture(long id){
        Lecture lecture = findById(id);
        lectureRepository.delete(lecture);
    }

    @Transactional(readOnly = true)
    public int countLectureByClassRoom(ClassRoomEntity classRoomEntity) {
        return lectureRepository.countByClassRoom(classRoomEntity);
    }

    public int getTotalLectureTime(ClassRoomEntity classRoomEntity) {
        return lectureRepository.findAllByClassRoom(classRoomEntity).stream()
                .mapToInt(lecture -> lecture.getTime().getHour() * 60 + lecture.getTime().getMinute())
                .sum();
    }

    public int getChapterLectureTime(ClassRoomEntity classRoomEntity) {
        return lectureRepository.findAllByClassRoom(classRoomEntity).stream()
                .mapToInt(lecture -> lecture.getTime().getHour() * 60 + lecture.getTime().getMinute())
                .sum();
    }

    @Transactional(readOnly = true)
    public int lectureProgress(Member member, ClassRoomEntity classRoom) {
        List<Lecture> lectures = lectureRepository.findAllByClassRoom(classRoom);
        int totalLectures = lectures.size();

        // 특정 강의에 대해 수강 완료된 강의의 개수를 구하는 방식으로 변경
        int completedLectures = lectures.stream()
                .mapToInt(lecture -> sugangRepository.countByMemberAndLectureAndCompleted(member, lecture, true))
                .sum();

        if (totalLectures == 0) return 0; // 강의가 없는 경우 진행률 0%
        return (completedLectures * 100) / totalLectures;
    }

}
