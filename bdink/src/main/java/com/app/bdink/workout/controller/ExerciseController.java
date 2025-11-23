package com.app.bdink.workout.controller;

import com.app.bdink.external.aws.service.S3Service;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.workout.controller.dto.ExercisePart;
import com.app.bdink.workout.controller.dto.request.CreateExerciseDto;
import com.app.bdink.workout.controller.dto.request.ExerciseReqDto;
import com.app.bdink.workout.controller.dto.response.ExerciseResDto;
import com.app.bdink.workout.service.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exercise")
@Tag(name = "일지작성 운동종목 API", description = "운동종목과 관련된 API들입니다. 운동종목 CRUD API입니다.")
public class ExerciseController {

    private final WorkoutService exerciseService;
    private final S3Service s3Service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(method = "POST", description = "신규 운동종목을 생성합니다.")
    public RspTemplate<?> createNewExercise(
            @RequestPart(value = "ExerciseVideo") MultipartFile exerciseVideo,
            @RequestPart(value = "ExercisePicture") MultipartFile exercisePicture,
            @RequestPart(value = "ExerciseReqDto") ExerciseReqDto exerciseReqDto) {

        //create Exercise
        CreateExerciseDto createExerciseDto = CreateExerciseDto.of(
                s3Service.uploadImageOrMedia("media/", exerciseVideo),
                s3Service.uploadImageOrMedia("image/", exercisePicture),
                exerciseReqDto
        );
        return RspTemplate.success(Success.CREATE_EXERCISE_SUCCESS, exerciseService.createExercise(createExerciseDto.exerciseReqDto(), createExerciseDto.ExerciseVideoKey(), createExerciseDto.ExercisePictureKey()));
    }

    @GetMapping("/part")
    @Operation(method = "GET", description = "부위별 운동종목을 조회합니다.")
    public RspTemplate<List<ExerciseResDto>> getPartExercise(@RequestParam ExercisePart exercisePart) {
        return RspTemplate.success(Success.GET_EXERCISEPART_SUCCESS, exerciseService.getPart(exercisePart));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(method = "PUT", description = "운동좀목을 수정합니다.")
    public RspTemplate<?> updateExercise(
            @RequestParam Long exerciseId,
            @RequestPart(value = "ExerciseVideo") MultipartFile exerciseVideo,
            @RequestPart(value = "ExercisePicture") MultipartFile exercisePicture,
            @RequestPart(value = "ExerciseReqDto") ExerciseReqDto exerciseReqDto
    ){
        String exerciseVideoKey = s3Service.uploadImageOrMedia("media/", exerciseVideo) ;
        String exercisePictureKey = s3Service.uploadImageOrMedia("image/", exercisePicture) ;

        //수정 비즈니스로직 연결 필요
        return RspTemplate.success(Success.UPDATE_EXERCISE_SUCCESS, exerciseService.updateExerciseInfo(exerciseReqDto, exerciseVideoKey, exercisePictureKey, exerciseId));
    }
}