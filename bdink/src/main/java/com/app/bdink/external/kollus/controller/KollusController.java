package com.app.bdink.external.kollus.controller;

import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.external.kollus.dto.request.UserKeyDTO;
import com.app.bdink.external.kollus.dto.request.callback.DeleteRequestDTO;
import com.app.bdink.external.kollus.dto.request.callback.LmsRequestDTO;
import com.app.bdink.external.kollus.dto.request.callback.PlayRequestDTO;
import com.app.bdink.external.kollus.dto.request.callback.UploadRequestDTO;
import com.app.bdink.external.kollus.entity.KollusMedia;
import com.app.bdink.external.kollus.service.KollusService;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.instructor.util.InstructorUtilService;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.lecture.service.LectureService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
@RequestMapping("/api/v1/kollus")
@RequiredArgsConstructor
@Tag(name = "Kollus API", description = "Kollus 관련 API들입니다. 콜백 api 같은 경우는 Kollus console 채널에서 콜백 redirect url을 설정해줘야 합니다.")
public class KollusController {

    private final KollusService kollusService;
    private final LectureService lectureService;
    private final InstructorUtilService instructorUtilService;
    private final MemberService memberService;

//    @PostMapping("/userkey")
//    @Operation(method = "POST", description = "Kollus 유저 키 생성 API 입니다. client_user_id는 kollusClientUserId 입니다.")
//    public RspTemplate<?> userKeyCreate(@RequestBody KollusRequest.userKeyDTO userKeyDTO) throws IOException {
//        log.info("유저키 생성 api controller 시작");
//        kollusService.createKollusUserKeyApi(userKeyDTO);
//        return RspTemplate.success(Success.KOLLUS_USERKEY_SUCCESS);
//    }

    @PostMapping("/{lectureId}/play-url")
    @Operation(method = "POST", description = "Kollus 동영상 접근 url입니다. lectureId를 통해서 접근합니다. 사용자키와 멤버가 매핑되어야 사용가능합니다.")
    public RspTemplate<?> playUrlCreate(@PathVariable Long lectureId, Principal principal) {
        return RspTemplate.success(Success.KOLLUS_GET_URL_SUCCESS, kollusService.createKollusURLService(principal, lectureId));
    }

    @PostMapping("/upload")
    @Operation(method = "POST", description = "Kollus upload callback API 입니다.")
    public RspTemplate<?> uploadCallback(@ModelAttribute UploadRequestDTO uploadRequestDTO) {
        kollusService.uploadCallbackService(uploadRequestDTO);
        return RspTemplate.success(Success.KOLLUS_CONTENT_UPLOAD_SUCCESS);
    }

    @PostMapping("/delete")
    @Operation(method = "POST", description = "Kollus delete Callback API 입니다.")
    public RspTemplate<?> deleteCallback(@ModelAttribute DeleteRequestDTO deleteRequestDTO) {
        kollusService.deleteCallbackService(deleteRequestDTO);
        return RspTemplate.success(Success.KOLLUS_CONTENT_DELETE_SUCCESS);
    }

    @PostMapping("/userkey")
    @Operation(method = "POST", description = "Kollus 사용자 키 생성 API 입니다. 사용자키를 입력해주세요.")
    public RspTemplate<?> userKeyCreate(@RequestBody UserKeyDTO userKeyDTO) {
        log.info("유저키 생성 api controller 시작");
        kollusService.createKollusUserKey(userKeyDTO);
        return RspTemplate.success(Success.KOLLUS_USERKEY_SUCCESS);
    }

    @PostMapping("/media")
    @Operation(method = "POST", description = "Kollus media를 강의와 연결합니다.")
    public RspTemplate<?> connectLecture(Principal principal, @RequestParam Long lectureId, @RequestParam Long kollusMediaId) {

        if (!instructorUtilService.validateLectureOwner(principal, lectureId)) {
            throw new CustomException(Error.NO_INSTRUCTOR, Error.NO_INSTRUCTOR.getMessage());
        }

        KollusMedia kollusMedia = kollusService.findById(kollusMediaId);

        Lecture lecture = lectureService.findById(lectureId);

        //Todo:여기 kollus서비스 로직
        return RspTemplate.success(Success.CONNECT_KOLLUSMEDIA_SUCCESS, CreateIdDto.from(kollusService.connectKollusAndLecture(lecture, kollusMedia)));
    }

    @GetMapping("/media-link/{memberId}")
    @Operation(method = "GET", description = "특정 사용자의 시청 기록을 확인합니다.")
    public RspTemplate<?> getMediaLink(@PathVariable Long memberId, Principal principal) {

        return RspTemplate.success(Success.GET_KOLLUSMEDIA_SUCCESS);
    }

    @PostMapping("/media-link")
    @Operation(method = "POST", description = "콜러스 미디어 시청 기록 생성")
    public RspTemplate<?> saveMediaLink(Principal principal, @RequestParam Long lectureId) {
        Member member = memberService.findById(Long.valueOf(principal.getName()));

        KollusMedia kollusMedia = kollusService.findByLectureId(lectureId);

        kollusService.saveMediaLink(member, kollusMedia);

        return RspTemplate.success(Success.KOLLUS_MEDIALINK_SAVE_SUCCESS);
    }

    @PostMapping("/lms")
    @Operation(method = "POST", description = "Kollus LMS Callback API 입니다.")
    public RspTemplate<?> lmsCallback(@ModelAttribute LmsRequestDTO lmsRequestDTO) {
        kollusService.lmsCallbackService(lmsRequestDTO);
        return RspTemplate.success(Success.KOLLUS_LMS_SUCCESS);
    }

    //@PostMapping("/play")
    //@Operation(method = "POST", description = "Kollus Play Callback API 입니다.")
    //public ResponseEntity<Map<String, Object>> playCallback(@ModelAttribute PlayRequestDTO playRequestDTO) {
    //    return kollusService.playCallbackService(playRequestDTO);
    //}

    @PostMapping("/play")
    @Operation(method = "POST", description = "Kollus Play Callback API 입니다.")
    public ResponseEntity<?> playCallback(@ModelAttribute PlayRequestDTO playRequestDTO) {
        log.info(playRequestDTO.toString());
        return kollusService.playCallbackService(playRequestDTO);
    }
}
