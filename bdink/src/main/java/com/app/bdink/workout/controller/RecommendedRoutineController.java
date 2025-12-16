package com.app.bdink.workout.controller;

import com.app.bdink.external.aws.service.S3Service;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.workout.controller.dto.request.RecommendedRoutineSaveReqDto;
import com.app.bdink.workout.controller.dto.response.RecommendedRoutineDetailResDto;
import com.app.bdink.workout.service.RecommendedRoutineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommendedRoutine")
@Tag(name = "추천 운동 루틴 API", description = "추천 운동 루틴 관련된 API들입니다.")
public class RecommendedRoutineController {

    private final RecommendedRoutineService recommendedRoutineService;
    private final S3Service s3Service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "추천 운동루틴 생성")
    public RspTemplate<?> createRoutine(@RequestPart(value = "RecommendedRoutineReqDto") RecommendedRoutineSaveReqDto dto,
                                        @RequestPart(value = "ThumbnailPicture") MultipartFile thumbnailPicture) {
        String thumbnailKey = s3Service.uploadImageOrMedia("image/", thumbnailPicture);

        Long id = recommendedRoutineService.createRoutine(dto, thumbnailKey);

        return RspTemplate.success(Success.CREATE_ROUTINE_SUCCESS, id);
    }

    // UPDATE
    @PutMapping("/{routineId}")
    @Operation(summary = "추천 운동루틴 수정")
    public RspTemplate<?> updateRoutine(@PathVariable Long routineId,
                                        @RequestBody RecommendedRoutineSaveReqDto dto) {
        recommendedRoutineService.updateRoutine(routineId, dto);
        return RspTemplate.success(Success.UPDATE_ROUTINE_SUCCESS, Success.UPDATE_ROUTINE_SUCCESS.getMessage());
    }

    // DELETE
    @DeleteMapping("/{routineId}")
    @Operation(summary = "추천 운동루틴 삭제")
    public RspTemplate<?> deleteRoutine(@PathVariable Long routineId) {
        recommendedRoutineService.deleteRoutine(routineId);
        return RspTemplate.success(Success.DELETE_ROUTINE_SUCCESS, Success.DELETE_ROUTINE_SUCCESS.getMessage());
    }

    // DETAIL – 추천 카드 눌렀을 때
    @GetMapping("/{routineId}")
    @Operation(summary = "추천 운동루틴 상세 조회",
            description = "routineId를 통해 추천 운동루틴을 조회하는 API입니다.")
    public RspTemplate<?> getRoutineDetail(@PathVariable Long routineId) {
        RecommendedRoutineDetailResDto dto = recommendedRoutineService.getRoutineDetail(routineId);
        return RspTemplate.success(Success.GET_ROUTINE_DETAIL_SUCCESS, dto);
    }

    @GetMapping("/all")
    @Operation(summary = "모든 추천 운동루틴 조회", description = "모든 추천 운동루틴을 조회합니다.")
    public RspTemplate<?> getRoutineAll(){
        return RspTemplate.success(Success.GET_ROUTINE_ALL_SUCCESS, recommendedRoutineService.getRoutineAll());
    }
}
