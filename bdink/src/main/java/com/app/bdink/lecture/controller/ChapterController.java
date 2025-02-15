package com.app.bdink.lecture.controller;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.entity.Instructor;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.lecture.service.ChapterService;
import com.app.bdink.lecture.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chapter")
public class ChapterController {

    private final ChapterService chapterService;
    private final ClassRoomService classRoomService;
    private final InstructorService instructorService;

    @PostMapping
    public ResponseEntity<?> createChapter(@RequestParam Long instructorId, @RequestParam Long classRoomId,
                                           @RequestParam String title) {
        Instructor instructor = instructorService.findById(instructorId);
        ClassRoom classRoom = classRoomService.findById(classRoomId);
        //권한 없으면 내치기

        if (!classRoom.isOwner(instructor)){
            throw new IllegalStateException("챕터 생성 권한이 없는 유저입니다.");
        }

        String chapterId = chapterService.createChapter(classRoom, title);
        return ResponseEntity.created(
                URI.create(chapterId))
                .build();
    }

}
