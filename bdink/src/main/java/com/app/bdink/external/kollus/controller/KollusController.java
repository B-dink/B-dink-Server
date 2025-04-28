package com.app.bdink.external.kollus.controller;

import com.app.bdink.external.kollus.dto.request.CallbackRequest;
import com.app.bdink.external.kollus.dto.request.KollusRequest;
import com.app.bdink.external.kollus.service.KollusService;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@Slf4j
@RequestMapping("/api/v1/kollus")
@RequiredArgsConstructor
@Tag(name = "Kollus API", description = "Kollus 관련 API들입니다. 콜백 api 같은 경우는 Kollus console 채널에서 콜백 redirect url을 설정해줘야 합니다.")
public class KollusController {

    private final KollusService kollusService;

//    @PostMapping("/userkey")
//    @Operation(method = "POST", description = "Kollus 유저 키 생성 API 입니다. client_user_id는 kollusClientUserId 입니다.")
//    public RspTemplate<?> userKeyCreate(@RequestBody KollusRequest.userKeyDTO userKeyDTO) throws IOException {
//        log.info("유저키 생성 api controller 시작");
//        kollusService.createKollusUserKeyApi(userKeyDTO);
//        return RspTemplate.success(Success.KOLLUS_USERKEY_SUCCESS);
//    }

    @GetMapping("/{lectureId}/play-url")
    @Operation(method = "GET", description = "Kollus 동영상 접근 url입니다. lectureId를 통해서 접근합니다. 사용자키와 멤버가 매핑되어야 사용가능합니다.")
    public RspTemplate<?> playUrlCreate(@PathVariable Long lectureId, Principal principal) throws IOException {
        return RspTemplate.success(Success.KOLLUS_GET_URL_SUCCESS, kollusService.createKollusURLService(principal, lectureId));
    }

    @PostMapping("/upload")
    @Operation(method = "POST", description = "Kollus upload callback API 입니다.")
    public RspTemplate<?> uploadCallback(@ModelAttribute CallbackRequest.uploadRequestDTO uploadRequestDTO) {
        kollusService.uploadCallbackService(uploadRequestDTO);
        return RspTemplate.success(Success.KOLLUS_UPLOAD_SUCCESS);
    }

    @PostMapping("/delete")
    @Operation(method = "POST", description = "Kollus delete Callback API 입니다.")
    public RspTemplate<?> deleteCallback(@ModelAttribute CallbackRequest.deleteRequestDTO deleteRequestDTO) {
        kollusService.deleteCallbackService(deleteRequestDTO);
        return RspTemplate.success(Success.KOLLUS_DELETE_SUCCESS);
    }

    @PostMapping("/userkey")
    @Operation(method = "POST", description = "Kollus 사용자 키 생성 API 입니다. 사용자키를 입력해주세요.")
    public RspTemplate<?> userKeyCreate(@RequestBody KollusRequest.userKeyDTO userKeyDTO) throws IOException {
        log.info("유저키 생성 api controller 시작");
        kollusService.createKollusUserKey(userKeyDTO);
        return RspTemplate.success(Success.KOLLUS_USERKEY_SUCCESS);
    }

    /**
     *  todo:진행률 관련 callback api 생성 예정
     */
//    @PostMapping("/lms")
//    @Operation(method = "POST", description = "Kollus LMS Callback API 입니다.")
//    public RspTemplate<?> lmsCallback(@ModelAttribute CallbackRequest.playRequestDTO playRequestDTO) {
//        log.info("------------------------------------------------------------");
//        log.info(String.valueOf(playRequestDTO.getContent_provider_key()));
//        log.info(String.valueOf(playRequestDTO.getMedia_content_key()));
//        log.info(String.valueOf(playRequestDTO.getMedia_profile_key()));
//        log.info(String.valueOf(playRequestDTO.getUser_key()));
//        log.info(String.valueOf(playRequestDTO.getLog_type()));
//        log.info(String.valueOf(playRequestDTO.getFull_filename()));
//        log.info(String.valueOf(playRequestDTO.getPlay_time()));
//        log.info(String.valueOf(playRequestDTO.getDuration()));
//        kollusService.playCallbackService(playRequestDTO);
//        log.info("------------------------------------------------------------");
//        return RspTemplate.success(Success.KOLLUS_LMS_SUCCESS);
//    }
}
