package com.app.bdink.lecture.service;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.service.ClassRoomService;
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



    public String createChapter(final ClassRoom classRoom, final String title){
        Chapter chapter = new Chapter(classRoom, title);
        boolean isNotFirstChapter = chapterRepository.existsByClassRoom(classRoom);

        if (isNotFirstChapter){ //첫번째 챕터아니면 ++
            chapter.updateNumber();
        }

        chapter = chapterRepository.save(chapter);
        classRoom.addChapter(chapter);
        return String.valueOf(chapter.getId());

    }


}
