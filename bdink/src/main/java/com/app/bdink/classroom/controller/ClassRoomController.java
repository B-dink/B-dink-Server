package com.app.bdink.classroom.controller;

import com.app.bdink.classroom.controller.dto.request.ClassRoomDto;
import com.app.bdink.classroom.controller.dto.response.ClassRoomResponse;
import com.app.bdink.classroom.entity.Instructor;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.lecture.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/classroom")
// 컨트롤러는 협력하는 서비스들의 파사드 패턴이다.
public class ClassRoomController {

    private final ClassRoomService classRoomService;
    private final InstructorService instructorService;

    @PostMapping
    ResponseEntity<?> createClassRoom(@RequestParam Long instructorId, @RequestBody ClassRoomDto classRoomDto){

        Instructor instructor = instructorService.findById(instructorId);
        String id = classRoomService.createClassRoom(instructor, classRoomDto);
        return ResponseEntity.created(
                URI.create(id))
                .build();
    }

    @GetMapping
    ResponseEntity<?> getClassRoomInfo(@RequestParam Long id){
        ClassRoomResponse classRoomDto = classRoomService.getClassRoomInfo(id);
        return ResponseEntity.ok(classRoomDto);
    }

    @PutMapping
    ResponseEntity<?> updateClassRoomInfo(@RequestParam Long id, @RequestBody ClassRoomDto classRoomDto){
        ClassRoomResponse classResponse = classRoomService.updateClassRoomInfo(id, classRoomDto);
        return ResponseEntity.ok(classResponse);
    }

    @DeleteMapping
    ResponseEntity<?> deleteClassRoom(@RequestParam Long id){
        classRoomService.deleteClassRoom(id);
        return ResponseEntity.noContent().build();
    }



}
