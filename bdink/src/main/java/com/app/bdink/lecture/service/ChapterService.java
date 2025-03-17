package com.app.bdink.lecture.service;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.lecture.entity.Chapter;
import com.app.bdink.lecture.repository.ChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChapterService {

    private final ChapterRepository chapterRepository;

    public Chapter findById(Long id){
        return chapterRepository.findById(id).orElseThrow(
                ()-> new IllegalStateException("해당 챕터를 찾지 못했습니다.")
        );
    }



    public String createChapter(final ClassRoomEntity classRoomEntity, final String title){
        Chapter chapter = new Chapter(classRoomEntity, title);
        boolean isNotFirstChapter = chapterRepository.existsByClassRoom(classRoomEntity);

        if (isNotFirstChapter){ //첫번째 챕터아니면 ++
            chapter.updateNumber();
        }

        chapter = chapterRepository.save(chapter);
        classRoomEntity.addChapter(chapter);
        return String.valueOf(chapter.getId());

    }


}
