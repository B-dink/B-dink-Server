package com.app.bdink.lecture.controller;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.lecture.controller.dto.LectureDto;
import com.app.bdink.lecture.controller.dto.response.LectureInfo;
import com.app.bdink.lecture.entity.Chapter;
import com.app.bdink.lecture.service.ChapterService;
import com.app.bdink.lecture.service.LectureService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/lecture")
public class LectureController {

    private final LectureService lectureService;
    private final ClassRoomService classRoomService;
    private final ChapterService chapterService;

    //TODO: 강사인지 확인하는 로직이 필요하다.
    @PostMapping
    public ResponseEntity<?> createLecture(@RequestParam Long classRoomId,
                                            @RequestParam Long chapterId,
                                            @RequestBody LectureDto lectureDto) {

        ClassRoom classRoom = classRoomService.findById(classRoomId);
        Chapter chapter = chapterService.findById(chapterId);

        String id = lectureService.createLecture(classRoom, chapter, lectureDto);
        return ResponseEntity.created(URI.create(id)).build();
    }

    @GetMapping
    public ResponseEntity<?> getLectureInfo(@RequestParam Long id) {

        LectureInfo lectureInfo = lectureService.getLectureInfo(id);
        return ResponseEntity.ok()
                .body(lectureInfo);
    }

    @DeleteMapping
    ResponseEntity<?> deleteLecture(@RequestParam Long id){
        lectureService.deleteLecture(id);
        return ResponseEntity.noContent().build();
    }
}
