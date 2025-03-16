package com.app.bdink.classroom.controller;

import com.app.bdink.classroom.controller.dto.request.ClassRoomDto;
import com.app.bdink.classroom.controller.dto.response.ClassRoomResponse;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.classroom.util.InstructorUtilService;
import com.app.bdink.external.aws.lambda.service.MediaService;
import com.app.bdink.external.aws.service.S3Service;
import com.app.bdink.lecture.service.InstructorService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/classroom")
@Tag(name = "클래스룸 API", description = "클래스룸과 관련된 API들입니다. 클래스룸은 강사정보를 입력한 유저만 생성할 수 있습니다.")
// 컨트롤러는 협력하는 서비스들의 파사드 패턴이다.
public class ClassRoomController {

    private final ClassRoomService classRoomService;
    private final S3Service s3Service;
    private final MediaService mediaService;
    private final MemberService memberService;
    private final InstructorUtilService instructorUtilService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(method = "POST", description = "클래스룸을 생성합니다.")
    ResponseEntity<?> createClassRoom(Principal memberId,
                                      @RequestPart(value = "thumbnail") MultipartFile thumbnail,
                                      @RequestPart(value = "intro-video") MultipartFile video,
                                      @RequestPart(value = "classRoomDto") ClassRoomDto classRoomDto) {

        String thumbnailKey = s3Service.uploadImageOrMedia("image/", thumbnail);
        String mediaKey = s3Service.uploadImageOrMedia("media/", video);


        String id = classRoomService.createClassRoom(
                instructorUtilService.getInstructor(memberId),
                thumbnailKey,
                mediaKey,
                classRoomDto
        );

        mediaService.createMedia(Long.parseLong(id), mediaKey);
        return ResponseEntity.created(
                        URI.create(id))
                .build();
    }

    @GetMapping
    @Operation(method = "GET", description = "해당 클래스룸 정보를 조회합니다.")
    ResponseEntity<?> getClassRoomInfo(@RequestParam Long id) {
        ClassRoomResponse classRoomDto = classRoomService.getClassRoomInfo(id);
        return ResponseEntity.ok(classRoomDto);
    }

    @GetMapping("/chapter")
    @Operation(method = "GET", description = "해당 클래스룸의 챕터 정보를 조회합니다.")
    ResponseEntity<?> getChapterInfo(@RequestParam Long id) {
        return ResponseEntity.ok(classRoomService.getChapterInfo(id));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(method = "PUT", description = "클래스룸 정보를 수정합니다.")
    ResponseEntity<?> updateClassRoomInfo(
            Principal principal,
            @RequestParam Long id,
            @RequestPart(value = "classRoomDto") ClassRoomDto classRoomDto,
            @RequestPart(value = "thumbnail") MultipartFile thumbnail,
            @RequestPart(value = "intro-video") MultipartFile video) {

        if (instructorUtilService.validateClassRoomOwner(principal, id)){
            throw new CustomException(Error.UNAUTHORIZED_ACCESS, Error.UNAUTHORIZED_ACCESS.getMessage());
        }

        String thumbnailKey = s3Service.uploadImageOrMedia("image/", thumbnail);
        String videoKey = s3Service.uploadImageOrMedia("media/", video);
        ClassRoom classRoom = classRoomService.findById(id);

        if (!classRoom.isEmptyThumbnail()) {
            s3Service.deleteImageAndMedia(classRoom.getThumbnail());
        }

        ClassRoomResponse classResponse = classRoomService.updateClassRoomInfo(classRoom, thumbnailKey, videoKey, classRoomDto);
        return ResponseEntity.ok(classResponse);
    }

    @DeleteMapping
    @Operation(method = "DELETE", description = "클래스룸을 삭제합니다. 이는 hard delete로 구성되어있으며 클래스룸을 삭제하면 안에 있는 챕터, 강좌들이 함께 삭제됩니다.")
    ResponseEntity<?> deleteClassRoom(
            Principal principal,
            @RequestParam Long id) {

        if (instructorUtilService.validateClassRoomOwner(principal, id)){
            throw new CustomException(Error.UNAUTHORIZED_ACCESS, Error.UNAUTHORIZED_ACCESS.getMessage());
        }

        ClassRoom classRoom = classRoomService.findById(id);
        s3Service.deleteImageAndMedia(classRoom.getThumbnail());
        classRoomService.deleteClassRoom(classRoom);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    @Operation(method = "GET", description = "클래스룸을 전체 조회합니다.")
    public ResponseEntity<?> getAllClassRoom() {
        return ResponseEntity.ok().body(classRoomService.getAllClassRoom());
    }

    @GetMapping("/career")
    @Operation(method = "GET", description = "특정 Career의 클래스룸을 조회합니다.")
    public ResponseEntity<?> getClassRoomByCareer(@RequestParam Career career) {
        return ResponseEntity.ok().body(classRoomService.getClassRoomByCareer(career));
    }

}
