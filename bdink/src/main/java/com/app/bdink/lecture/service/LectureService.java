package com.app.bdink.lecture.service;

import com.app.bdink.chapter.entity.Chapter;
import com.app.bdink.chapter.repository.ChapterRepository;
import com.app.bdink.chapter.service.ChapterService;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.external.kollus.entity.KollusMedia;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.lecture.controller.dto.LectureDto;
import com.app.bdink.lecture.controller.dto.response.LectureInfo;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final ChapterService chapterService;

    public Lecture findById(Long id){
        return lectureRepository.findById(id).orElseThrow(
                ()-> new CustomException(Error.NOT_FOUND_LECTURE, Error.NOT_FOUND_LECTURE.getMessage())
        );
    }

    //chapter에 강좌 추가
    @Transactional
    public String createLecture(final Long chapterId,
                                final LectureDto lectureDto, final KollusMedia kollusMedia){
        String mediaContentKey = kollusMedia.getMediaContentKey(); //todo:kollus id를 가져올 필요가 없을것 같음.

        Chapter chapter = chapterService.findWithClassRoomById(chapterId);

        ClassRoomEntity classRoom = chapter.getClassRoom();

        log.info("클래스룸 id는 값 : {}", classRoom.getId());

        Lecture lecture = lectureRepository.save(
                Lecture.builder()
                        .classRoom(classRoom)
                        .chapter(chapter) //강좌를 만들때 챕터는 무조건 있어야한다.
                        .title(lectureDto.title())
                        .time(lectureDto.convertToLocalTime())
                        .mediaLink(mediaContentKey)
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
}
