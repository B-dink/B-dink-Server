package com.app.bdink.youtube.controller;

import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.youtube.entity.Youtube;
import com.app.bdink.youtube.service.YoutubeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/youtube")
@Tag(name = "유튜브 비디오 API", description = "유튜브 비디오 API 입니다.")
public class YoutubeController {
    private final YoutubeService youtubeService;

    @GetMapping
    @Operation(method = "GET", description = "유튜브 비디오 조회 API")
    public RspTemplate<?> getYoutubeVideo(@RequestParam Long youtubeId) {
        return RspTemplate.success(Success.GET_YOUTUBE_VIDEO_SUCCESS, youtubeService.getYoutubeInfoDto(youtubeId));
    }

    @PostMapping
    @Operation(method = "POST", description = "유튜브 비디오 생성 API")
    public RspTemplate<?> createYoutubeVideo(@RequestParam String youtubeVideoUrl) {
        return RspTemplate.success(Success.CREATE_YOUTUBEVIDEO_SUCCESS, CreateIdDto.from(youtubeService.saveYoutubeVideo(youtubeVideoUrl)));
    }

    @PutMapping
    @Operation(method = "PUT", description = "유튜브 비디오 수정 API")
    public RspTemplate<?> updateYoutubeVideo(@RequestParam Long youtubeId, @RequestParam String youtubeVideoUrl) {
        Youtube youtube = youtubeService.findById(youtubeId);
        youtubeService.updateYoutubeVideo(youtube, youtubeVideoUrl);
        return RspTemplate.success(Success.UPDATE_YOUTUBEVIDEO_SUCCESS);
    }

    @DeleteMapping
    @Operation(method = "DELETE", description = "유튜브 비디오 삭제 API, hard delete로 구성되어있습니다.")
    public RspTemplate<?> deleteYoutubeVideo(@RequestParam Long youtubeId) {
        Youtube youtube = youtubeService.findById(youtubeId);
        youtubeService.deleteYoutubeVideo(youtube);
        return RspTemplate.success(Success.DELETE_YOUTUBEVIDEO_SUCCESS);
    }
}
