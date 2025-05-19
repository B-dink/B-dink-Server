package com.app.bdink.instructor.adapter.in.controller;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.external.aws.service.S3Service;
import com.app.bdink.instructor.adapter.in.controller.dto.response.InstructorClassroomDto;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.instructor.util.InstructorUtilService;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.instructor.adapter.in.controller.dto.InstructorDto;
import com.app.bdink.instructor.adapter.in.controller.dto.request.UpdateInstructorDto;
import com.app.bdink.instructor.adapter.in.controller.dto.response.InstructorInfoDto;
import com.app.bdink.instructor.service.InstructorService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/instructor")
@Tag(name = "강사 API", description = "강사와 관련된 API들입니다. 강사는 회원가입해 Member 엔티티가 생성된 이후에 생성할 수 있습니다.")
public class InstructorController {

    private final InstructorService instructorService;
    private final MemberService memberService;
    private final MemberUtilService memberUtilService;
    private final ClassRoomService classRoomService;
    private final S3Service s3Service;
    private final InstructorUtilService instructorUtilService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(method = "POST", description = "강사 정보를 생성합니다.")
    public RspTemplate<?> createInstructor(Principal principal,
                                           @RequestPart(value = "instructorDto") InstructorDto instructorDto,
                                           @RequestPart(value = "marketingImage", required = false) MultipartFile marketingImage){

        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        String thumbnailKey = null;

        if (marketingImage !=null && !marketingImage.isEmpty()){
            thumbnailKey = s3Service.uploadImageOrMedia("image/", marketingImage);
        }

        String instructorId = instructorService.createInstructor(member, instructorDto, thumbnailKey);

        return RspTemplate.success(Success.CREATE_INSTRUCTOR_SUCCESS, CreateIdDto.from(instructorId));
    }

    @GetMapping
    @Operation(method = "GET", description = "강사 정보를 조회합니다.")
    public RspTemplate<?> getInstructorInfo(Principal principal){
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        InstructorInfoDto instructorInfo = instructorService.getInfo(member);
        return RspTemplate.success(Success.GET_INSTRUCTOR_SUCCESS, instructorInfo);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(method = "PUT", description = "강사 정보를 수정합니다.")
    public RspTemplate<?> modifyInstructorInfo(Principal principal,
                                               @RequestPart(value = "instructorDto") UpdateInstructorDto instructorDto,
                                               @RequestPart(value = "marketingImage", required = false) MultipartFile marketingImage){
        Member member = memberService.findById(memberUtilService.getMemberId(principal));

        String thumbnailKey = null;

        if (marketingImage !=null && !marketingImage.isEmpty()){
            thumbnailKey = s3Service.uploadImageOrMedia("image/", marketingImage);
        }

        if (!member.getInstructor().isEmptyThumbnail()) {
            s3Service.deleteImageAndMedia(thumbnailKey);
        }

        InstructorInfoDto infoDto = instructorService.modifyInstructorInfo(member, instructorDto, thumbnailKey);
        return RspTemplate.success(Success.UPDATE_INSTRUCTOR_SUCCESS, infoDto);
    }

    @GetMapping("/career")
    @Operation(method = "GET", description = "특정 Career의 강사를 조회합니다.")
    public RspTemplate<?> getClassRoomByCareer(@RequestParam Career career) {
        return RspTemplate.success(Success.GET_CLASSROOM_CARRER_SUCCESS, instructorService.getInstructorInfoByCareer(career));
    }

    @GetMapping("/{id}/classroom")
    @Operation(method = "GET", description = "강사별 클래스룸을 조회합니다.")
    public RspTemplate<List<InstructorClassroomDto>> getClassRoomByInstructor(Principal principal, @PathVariable Long id){
        Instructor instructor = instructorService.findById(id);
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        List<InstructorClassroomDto> classRoomEntityList = classRoomService.getFilteredClassroomByInstructor(member, instructor);
        return RspTemplate.success(Success.GET_CLASSROOM_FILTERED_INSTURCTOR_SUCCESS, classRoomEntityList);
    }


    @DeleteMapping
    @Operation(method = "DELETE", description = "강사 정보를 삭제합니다. soft delete를 진행합니다. 강사정보를 제거하더라도 자신이 등록한 강의를 보유하고 싶을 수도 있기 때문에")
    public RspTemplate<?> deleteInstructor(Principal principal){
        Instructor instructor = instructorUtilService.getInstructor(principal);
        List<ClassRoomEntity> classRoomEntityList = classRoomService.getClassRoomEntityByInstructor(instructor);
        instructorService.deleteInstructor(instructor, classRoomEntityList);
        return RspTemplate.success(Success.DELETE_INSTRUCTOR_SUCCESS, Success.DELETE_INSTRUCTOR_SUCCESS.getMessage());
    }
}
