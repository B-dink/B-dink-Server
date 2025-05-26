package com.app.bdink.chapter.service;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.chapter.entity.Chapter;
import com.app.bdink.chapter.repository.ChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChapterService {

    private final ChapterRepository chapterRepository;

    public Chapter findById(Long id){
        return chapterRepository.findById(id).orElseThrow(
                ()-> new CustomException(Error.NOT_FOUND_CHAPTER, Error.NOT_FOUND_CHAPTER.getMessage())
        );
    }

    public Chapter findWithClassRoomById(Long id){
        return chapterRepository.findWithClassRoomById(id).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_CHAPTER, Error.NOT_FOUND_CHAPTER.getMessage())
        );
    }



    public String createChapter(final ClassRoomEntity classRoomEntity, final String title, final String thumbnail){
        Chapter chapter = new Chapter(classRoomEntity, title, thumbnail);
        boolean isNotFirstChapter = chapterRepository.existsByClassRoom(classRoomEntity);

        if (isNotFirstChapter){ //첫번째 챕터아니면 ++
            chapter.updateNumber();
        }

        chapter = chapterRepository.save(chapter);
        classRoomEntity.addChapter(chapter);
        return String.valueOf(chapter.getId());

    }


}
