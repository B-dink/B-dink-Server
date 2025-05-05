package com.app.bdink.lecture.controller;


import com.app.bdink.chapter.service.ChapterService;
import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.external.aws.service.S3Service;
import com.app.bdink.external.kollus.entity.KollusMedia;
import com.app.bdink.external.kollus.service.KollusService;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.instructor.util.InstructorUtilService;
import com.app.bdink.lecture.controller.dto.LectureDto;
import com.app.bdink.lecture.controller.dto.response.LectureInfo;
import com.app.bdink.lecture.service.LectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/lecture")
@Tag(name = "강의 API", description = "강의와 관련된 API들입니다. 강의는 클래스룸을 생성하고, 챕터가 생성되어있어야하며, 해당 챕터에 생성해야합니다.")
public class LectureController {

    private final LectureService lectureService;
    private final InstructorUtilService instructorUtilService;
    private final ChapterService chapterService;
    private final KollusService kollusService;
    private final S3Service s3Service;

    //TODO: 강사인지 확인하는 로직이 필요하다.
    @PostMapping
    @Operation(method = "POST", description = "강의를 생성합니다. 강의를 생성하면 해당 강의에 대한 시간정보, 강의 수가 해당 챕터에 업데이트 되는 로직입니다.")
    public RspTemplate<?> createLecture(
                                           Principal principal,
                                           @RequestParam Long classRoomId,
                                           @RequestParam Long chapterId,
                                           @RequestParam Long kollusMediaId,
                                           @RequestBody LectureDto lectureDto) {

        if (!instructorUtilService.validateClassRoomOwner(principal, classRoomId)){
            throw new CustomException(Error.NO_INSTRUCTOR, Error.NO_INSTRUCTOR.getMessage());
        }

        KollusMedia kollusMedia = kollusService.findById(kollusMediaId);



//        String uploadUrlKey = null;

//        if (!file.isEmpty()){
//            uploadUrlKey = s3Service.uploadMedia("media/", file); //TODO: 이미지인지 동영상인지 구분 + s3에서 validation하도록 구현
//        }

//        String uploadUrl = s3Service.generateOriginalLink(uploadUrlKey);

        //todo:kollus 미디어 링크나 미디어를 연결하는 로직을 생성 기존에 생각했던건 미디어를 강좌와 연결

        return RspTemplate.success(Success.CREATE_LECTURE_SUCCESS, CreateIdDto.from(lectureService.createLecture(chapterId, lectureDto, kollusMedia)));
    }

    @GetMapping
    @Operation(method = "GET", description = "강의 id를 받아 해당 강의의 정보를 조회합니다.")
    public RspTemplate<?> getLectureInfo(@RequestParam Long id) {

        LectureInfo lectureInfo = lectureService.getLectureInfo(id);
        return RspTemplate.success(Success.GET_LECTURE_SUCCESS, lectureInfo);
    }

    @DeleteMapping
    @Operation(method = "DELETE", description = "강의를 삭제합니다. 현재 하드 delete로 구성되어있습니다.")
    RspTemplate<?> deleteLecture(Principal principal,
                                    @RequestParam Long id){

        if (!instructorUtilService.validateLectureOwner(principal, id)){
            throw new CustomException(Error.NO_INSTRUCTOR, Error.NO_INSTRUCTOR.getMessage());
        }

        lectureService.deleteLecture(id);
        return RspTemplate.success(Success.DELETE_LECTURE_SUCCESS, Success.DELETE_LECTURE_SUCCESS.getMessage());
    }
}
