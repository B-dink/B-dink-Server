package com.app.bdink.lecture.controller;

import com.app.bdink.lecture.controller.dto.InstructorDto;
import com.app.bdink.lecture.service.InstructorService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/instructor")
@Tag(name = "강사 API", description = "강사와 관련된 API들입니다. 강사는 회원가입해 Member 엔티티가 생성된 이후에 생성할 수 있습니다.")
public class InstructorController {

    private final InstructorService instructorService;
    private final MemberService memberService;

    @PostMapping
    @Operation(method = "POST", description = "강사 정보를 생성합니다.")
    public ResponseEntity<?> createInstructor(@RequestParam Long memberId, @RequestBody InstructorDto instructorDto){

        Member member = memberService.findById(memberId);
        String instructorId = instructorService.createInstructor(member, instructorDto);

        return ResponseEntity.created(
                URI.create(instructorId))
                .build();
    }
}
