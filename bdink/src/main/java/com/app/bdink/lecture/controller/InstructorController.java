package com.app.bdink.lecture.controller;

import com.app.bdink.lecture.controller.dto.InstructorDto;
import com.app.bdink.lecture.service.InstructorService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/instructor")
public class InstructorController {

    private final InstructorService instructorService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> createInstructor(@RequestParam Long memberId, @RequestBody InstructorDto instructorDto){

        Member member = memberService.findById(memberId);
        String instructorId = instructorService.createInstructor(member, instructorDto);

        return ResponseEntity.created(
                URI.create(instructorId))
                .build();
    }
}
