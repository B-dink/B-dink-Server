package com.app.bdink.lecture.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.lecture.controller.dto.LectureIdInfoDto;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureUtilService {
    private final LectureRepository lectureRepository;

    public Lecture findById(Long id){
        return lectureRepository.findById(id).orElseThrow(
                ()-> new CustomException(com.app.bdink.global.exception.Error.NOT_FOUND_LECTURE, Error.NOT_FOUND_LECTURE.getMessage())
        );
    }

    @Transactional
    public LectureIdInfoDto getInfoLectureId(Lecture lecture){
        Long currentLectureId = lecture.getId();
        Long classRoomId = lecture.getClassRoom().getId();

        List<Lecture> lectures = lectureRepository.findByClassRoomIdOrderBySortOrderAsc(classRoomId);

        int index = IntStream.range(0, lectures.size())
                .filter(i -> lectures.get(i).getId().equals(currentLectureId))
                .findFirst()
                .orElse(-1);

        if (index == -1) {
            throw new CustomException(Error.NOT_FOUND_LECTURE, Error.NOT_FOUND_LECTURE.getMessage());
        }

        Long prevLectureId = (index > 0) ? lectures.get(index - 1).getId() : null;
        Long nextLectureId = (index < lectures.size() - 1) ? lectures.get(index + 1).getId() : null;

        return LectureIdInfoDto.from(prevLectureId, nextLectureId);
    }

}
