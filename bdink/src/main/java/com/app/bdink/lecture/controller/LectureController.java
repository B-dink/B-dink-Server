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
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/lecture")
@Tag(name = "강의 API", description = "강의와 관련된 API들입니다. 강의는 클래스룸을 생성하고, 챕터가 생성되어있어야하며, 해당 챕터에 생성해야합니다.")
public class LectureController {

    private final LectureService lectureService;
    private final ClassRoomService classRoomService;
    private final ChapterService chapterService;

    //TODO: 강사인지 확인하는 로직이 필요하다.
    @PostMapping
    @Operation(method = "POST", description = "강의를 생성합니다. 강의를 생성하면 해당 강의에 대한 시간정보, 강의 수가 해당 챕터에 업데이트 되는 로직입니다.")
    public ResponseEntity<?> createLecture(@RequestParam Long classRoomId,
                                            @RequestParam Long chapterId,
                                            @RequestBody LectureDto lectureDto) {

        ClassRoom classRoom = classRoomService.findById(classRoomId);
        Chapter chapter = chapterService.findById(chapterId);

        String id = lectureService.createLecture(classRoom, chapter, lectureDto);
        return ResponseEntity.created(URI.create(id)).build();
    }

    @GetMapping
    @Operation(method = "GET", description = "강의 id를 받아 해당 강의의 정보를 조회합니다.")
    public ResponseEntity<?> getLectureInfo(@RequestParam Long id) {

        LectureInfo lectureInfo = lectureService.getLectureInfo(id);
        return ResponseEntity.ok()
                .body(lectureInfo);
    }

    @DeleteMapping
    @Operation(method = "DELETE", description = "강의를 삭제합니다. 현재 하드 delete로 구성되어있습니다.")
    ResponseEntity<?> deleteLecture(@RequestParam Long id){
        lectureService.deleteLecture(id);
        return ResponseEntity.noContent().build();
    }
}
