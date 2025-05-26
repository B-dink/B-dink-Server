package com.app.bdink.chapter.controller;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.external.aws.service.S3Service;
import com.app.bdink.instructor.util.InstructorUtilService;
import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.chapter.service.ChapterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chapter")
@Tag(name = "챕터 API", description = "챕터와 관련된 API들입니다. 챕터는 챕터마다 강좌에 대한 데이터를 총합해 가지고 있습니다. 클래스룸을 만들어야 생성할 수 있습니다.")
@Slf4j
public class ChapterController {

    private final ChapterService chapterService;
    private final ClassRoomService classRoomService;
    private final InstructorUtilService instructorUtilService;
    private final S3Service s3Service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(method = "POST", description = "챕터를 생성합니다.")
    public RspTemplate<?> createChapter(
                                           Principal principal,
                                           @RequestParam Long classRoomId,
                                           @RequestPart(value = "thumbnail") MultipartFile thumbnail,
                                           @RequestParam String title) {

        if (!instructorUtilService.validateClassRoomOwner(principal, classRoomId)){
            throw new CustomException(Error.UNAUTHORIZED_ACCESS, Error.UNAUTHORIZED_ACCESS.getMessage());
        }
        ClassRoomEntity classRoomEntity = classRoomService.findById(classRoomId);

        String chapterThumbnail = s3Service.uploadImageOrMedia("image/", thumbnail);

        String chapterId = chapterService.createChapter(classRoomEntity, title, chapterThumbnail);
        return RspTemplate.success(Success.CREATE_CHAPTER_SUCCESS, CreateIdDto.from(chapterId));
    }

}
