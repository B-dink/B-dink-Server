package com.app.bdink.lecture.controller;

import com.app.bdink.lecture.controller.dto.LectureDto;
import com.app.bdink.lecture.controller.dto.response.LectureInfo;
import com.app.bdink.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/lecture")
public class LectureController {

    private final LectureService lectureService;

    @PostMapping
    public ResponseEntity<?> createLecuture(@RequestParam Long memberId, @RequestParam Long classRoomId,
                                            @RequestBody LectureDto lectureDto) {

        String id = lectureService.createLecture(memberId, classRoomId, lectureDto);
        return ResponseEntity.created(URI.create(id)).build();
    }

    @GetMapping
    public ResponseEntity<?> getLectureInfo(@RequestParam Long id) {

        LectureInfo lectureInfo = lectureService.getLectureInfo(id);
        return ResponseEntity.ok()
                .body(lectureInfo);
    }
}
