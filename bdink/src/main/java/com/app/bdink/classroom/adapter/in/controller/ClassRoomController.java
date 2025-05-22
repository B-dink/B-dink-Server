package com.app.bdink.classroom.adapter.in.controller;

import com.app.bdink.bookmark.service.BookmarkService;
import com.app.bdink.classroom.adapter.in.controller.dto.request.ClassRoomDto;
import com.app.bdink.classroom.adapter.in.controller.dto.response.*;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.classroom.port.in.ClassRoomUseCase;
import com.app.bdink.classroom.service.ClassRoomDetailPageImageService;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.classroom.service.command.CreateClassRoomCommand;
import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.external.aws.lambda.service.MediaService;
import com.app.bdink.external.aws.service.S3Service;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.instructor.util.InstructorUtilService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.member.util.MemberUtilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/classroom")
@Tag(name = "클래스룸 API", description = "클래스룸과 관련된 API들입니다. 클래스룸은 강사정보를 입력한 유저만 생성할 수 있습니다.")
// 컨트롤러는 협력하는 서비스들의 파사드 패턴이다.
public class ClassRoomController {

    private final ClassRoomService classRoomService;
    private final ClassRoomUseCase classRoomUseCase;
    private final S3Service s3Service;
    private final MediaService mediaService;
    private final InstructorUtilService instructorUtilService;
    private final MemberService memberService;
    private final BookmarkService bookmarkService;
    private final ClassRoomDetailPageImageService classRoomDetailPageImageService;
    private final MemberUtilService memberUtilService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(method = "POST", description = "클래스룸을 생성합니다.")
    RspTemplate<?> createClassRoom(Principal memberId,
                                   @RequestPart(value = "thumbnail") MultipartFile thumbnail,
                                   @RequestPart(value = "detailPageImage") List<MultipartFile> detailPageImages,
                                   @RequestPart(value = "intro-video") MultipartFile video,
                                   @RequestPart(value = "classRoomDto") ClassRoomDto classRoomDto) {

        List<String> detailPageImageUrls = detailPageImages.stream()
                .map(file -> s3Service.uploadImageOrMedia("image/", file))
                .toList();


        // create Command
        CreateClassRoomCommand command = CreateClassRoomCommand.of(
                instructorUtilService.getInstructor(memberId),
                s3Service.uploadImageOrMedia("image/", thumbnail),
                s3Service.uploadImageOrMedia("media/", video),
                detailPageImageUrls,
                classRoomDto
        );

        // usecase의 메소드 호출.
        String id = classRoomUseCase.createClassRoom(command);

        //흠.. 밑에 애도 이런식으로 바꿔야하는데.
        mediaService.createMedia(Long.parseLong(id), command.mediaKey(), null, command.thumbnailKey());

        classRoomDetailPageImageService.saveImages(Long.parseLong(id), command.detailImageKey());

        return RspTemplate.success(Success.CREATE_CLASSROOM_SUCCESS, CreateIdDto.from(id));
    }

    @GetMapping
    @Operation(method = "GET", description = "해당 클래스룸 정보를 조회합니다.")
    RspTemplate<?> getClassRoomInfo(Principal principal, @RequestParam Long id) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        ClassRoomResponse classRoomDto = classRoomService.getClassRoomInfo(member, id);
        return RspTemplate.success(Success.GET_CLASSROOM_SUCCESS, classRoomDto);
    }

    @GetMapping("/chapter")
    @Operation(method = "GET", description = "해당 클래스룸의 챕터 정보를 조회합니다.")
    RspTemplate<?> getChapterInfo(@RequestParam Long id, Principal principal) {
        return RspTemplate.success(Success.GET_CHAPTER_SUCCESS, classRoomService.getChapterInfo(id, principal));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(method = "PUT", description = "클래스룸 정보를 수정합니다.")
    RspTemplate<?> updateClassRoomInfo(
            Principal principal,
            @RequestParam Long id,
            @RequestPart(value = "classRoomDto") ClassRoomDto classRoomDto,
            @RequestPart(value = "thumbnail") MultipartFile thumbnail,
            @RequestPart(value = "intro-video") MultipartFile video) {

        if (!instructorUtilService.validateClassRoomOwner(principal, id)) {
            throw new CustomException(Error.UNAUTHORIZED_ACCESS, Error.UNAUTHORIZED_ACCESS.getMessage());
        }

        String thumbnailKey = s3Service.uploadImageOrMedia("image/", thumbnail);
        String videoKey = s3Service.uploadImageOrMedia("media/", video);
        ClassRoomEntity classRoomEntity = classRoomService.findById(id);

        if (!classRoomEntity.isEmptyThumbnail()) {
            s3Service.deleteImageAndMedia(classRoomEntity.getThumbnail());
        }

        ClassRoomResponse classResponse = classRoomService.updateClassRoomInfo(classRoomEntity, thumbnailKey, videoKey, classRoomDto);
        return RspTemplate.success(Success.UPDATE_CLASSROOM_SUCCESS, classResponse);
    }

    @DeleteMapping
    @Operation(method = "DELETE", description = "클래스룸을 삭제합니다. 이는 hard delete로 구성되어있으며 클래스룸을 삭제하면 안에 있는 챕터, 강좌들이 함께 삭제됩니다.")
    RspTemplate<?> deleteClassRoom(
            Principal principal,
            @RequestParam Long id) {

        if (!instructorUtilService.validateClassRoomOwner(principal, id)) {
            throw new CustomException(Error.UNAUTHORIZED_ACCESS, Error.UNAUTHORIZED_ACCESS.getMessage());
        }

        ClassRoomEntity classRoomEntity = classRoomService.findById(id);
        s3Service.deleteImageAndMedia(classRoomEntity.getThumbnail());
        classRoomService.deleteClassRoom(classRoomEntity);

        return RspTemplate.success(Success.DELETE_CLASSROOM_SUCCESS);
    }

    @GetMapping("/all")
    @Operation(method = "GET", description = "클래스룸을 전체 조회합니다.")
    public RspTemplate<CareerListDto> getAllClassRoom(Principal principal) {
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        return RspTemplate.success(Success.GET_ALL_CLASSROOM_SUCCESS, classRoomService.getAllClassRoom(member));
    }

    @GetMapping("/career")
    @Operation(method = "GET", description = "특정 Career의 클래스룸을 조회합니다.")
    public RspTemplate<List<CareerClassroomDto>> getClassRoomByCareer(@RequestParam Career career) {
        return RspTemplate.success(Success.GET_CLASSROOM_CARRER_SUCCESS, classRoomService.getClassRoomByCareer(career));
    }

    @GetMapping("/class-detail/{id}")
    @Operation(method = "GET", description = "클래스 디테일 페이지를 조회합니다.")
    public RspTemplate<ClassRoomDetailResponse> getClassRoomDetail(@PathVariable Long id, Principal principal) {
        Long memberId = Long.parseLong((principal.getName()));
        Member member = memberService.findById(memberId);
        ClassRoomEntity classRoom = classRoomService.findById(id);
        Boolean isBookmarked = classRoomService.isClassRoomBookmarked(member, classRoom);
        long bookmarkCount = bookmarkService.getBookmarkCountForClassRoom(classRoom);
        ClassRoomDetailResponse classRoomDetailResponse = classRoomService.getClassRoomDetail(id, bookmarkCount, member, isBookmarked);
        return RspTemplate.success(Success.GET_CLASSROOM_DETAIL_SUCCESS, classRoomDetailResponse);
    }

    @GetMapping("/{id}/progress")
    @Operation(method = "GET", description = "특정 클래스룸의 학습 진행도를 조회합니다.")
    public RspTemplate<?> getLectureProgress(Principal principal, @PathVariable Long id) {
        Long memberId = Long.parseLong(principal.getName()); // 현재 로그인한 사용자의 ID 가져오기
        Member member = memberService.findById(memberId);

        List<ClassRoomProgressResponse> progressList = classRoomService.getLectureProgress(member, id);

        List<ClassRoomProgressResponse> progressWithStatus = progressList.stream()
                .map(progress -> {
                    String status = progress.status().equals("완강") ? "완강" : progress.status();
                    return new ClassRoomProgressResponse(progress.title(), progress.instructor(), status);
                })
                .collect(Collectors.toList());

        return RspTemplate.success(Success.GET_CLASSROOM_PROGRESS_SUCCESS, progressWithStatus);
    }
}
