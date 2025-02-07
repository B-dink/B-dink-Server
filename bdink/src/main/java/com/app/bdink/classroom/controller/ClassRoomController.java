package com.app.bdink.classroom.controller;

import com.app.bdink.classroom.controller.dto.ClassRoomDto;
import com.app.bdink.classroom.entity.ClassRoom;
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
        ClassRoom classRoom = classRoomService.getClassRoom(id);
        return ResponseEntity.ok(classRoom);
    }

}
