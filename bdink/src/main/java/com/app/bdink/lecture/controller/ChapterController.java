package com.app.bdink.lecture.controller;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.classroom.util.InstructorUtilService;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.lecture.service.ChapterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chapter")
@Tag(name = "챕터 API", description = "챕터와 관련된 API들입니다. 챕터는 챕터마다 강좌에 대한 데이터를 총합해 가지고 있습니다. 클래스룸을 만들어야 생성할 수 있습니다.")
public class ChapterController {

    private final ChapterService chapterService;
    private final ClassRoomService classRoomService;
    private final InstructorUtilService instructorUtilService;

    @PostMapping
    @Operation(method = "POST", description = "챕터를 생성합니다.")
    public ResponseEntity<?> createChapter(
                                           Principal principal,
                                           @RequestParam Long classRoomId,
                                           @RequestParam String title) {

        if (instructorUtilService.validateClassRoomOwner(principal, classRoomId)){
            throw new CustomException(Error.UNAUTHORIZED_ACCESS, Error.UNAUTHORIZED_ACCESS.getMessage());
        }

        ClassRoom classRoom = classRoomService.findById(classRoomId);

        String chapterId = chapterService.createChapter(classRoom, title);
        return ResponseEntity.created(
                URI.create(chapterId))
                .build();
    }

}
