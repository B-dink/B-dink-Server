package com.app.bdink.classroom.controller;

import com.app.bdink.classroom.controller.dto.request.ClassRoomDto;
import com.app.bdink.classroom.controller.dto.response.ClassRoomResponse;
import com.app.bdink.classroom.entity.Instructor;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.lecture.service.InstructorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/classroom")
@Tag(name = "클래스룸 API", description = "클래스룸과 관련된 API들입니다. 클래스룸은 강사정보를 입력한 유저만 생성할 수 있습니다.")
// 컨트롤러는 협력하는 서비스들의 파사드 패턴이다.
public class ClassRoomController {

    private final ClassRoomService classRoomService;
    private final InstructorService instructorService;

    @PostMapping
    @Operation(method = "POST", description = "클래스룸을 생성합니다.")
    ResponseEntity<?> createClassRoom(@RequestParam Long instructorId, @RequestBody ClassRoomDto classRoomDto){

        Instructor instructor = instructorService.findById(instructorId);
        String id = classRoomService.createClassRoom(instructor, classRoomDto);
        return ResponseEntity.created(
                URI.create(id))
                .build();
    }

    @GetMapping
    @Operation(method = "GET", description = "해당 클래스룸 정보를 조회합니다.")
    ResponseEntity<?> getClassRoomInfo(@RequestParam Long id){
        ClassRoomResponse classRoomDto = classRoomService.getClassRoomInfo(id);
        return ResponseEntity.ok(classRoomDto);
    }

    @PutMapping
    @Operation(method = "PUT", description = "클래스룸 정보를 수정합니다.")
    ResponseEntity<?> updateClassRoomInfo(@RequestParam Long id, @RequestBody ClassRoomDto classRoomDto){
        ClassRoomResponse classResponse = classRoomService.updateClassRoomInfo(id, classRoomDto);
        return ResponseEntity.ok(classResponse);
    }

    @DeleteMapping
    @Operation(method = "DELETE", description = "클래스룸을 삭제합니다. 이는 hard delete로 구성되어있으며 클래스룸을 삭제하면 안에 있는 챕터, 강좌들이 함께 삭제됩니다.")
    ResponseEntity<?> deleteClassRoom(@RequestParam Long id){
        classRoomService.deleteClassRoom(id);
        return ResponseEntity.noContent().build();
    }



}
