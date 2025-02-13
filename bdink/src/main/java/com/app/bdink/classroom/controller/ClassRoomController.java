package com.app.bdink.classroom.controller;

import com.app.bdink.classroom.controller.dto.request.ClassRoomDto;
import com.app.bdink.classroom.controller.dto.response.ClassRoomResponse;
import com.app.bdink.classroom.service.ClassRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/classroom")
public class ClassRoomController {

    private final ClassRoomService classRoomService;

    @PostMapping
    ResponseEntity<?> createClassRoom(@RequestBody ClassRoomDto classRoomDto){
        String id = classRoomService.createClassRoom(classRoomDto);
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
