package com.app.bdink.workout.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.workout.controller.dto.request.RecommendedRoutineSaveReqDto;
import com.app.bdink.workout.controller.dto.request.RoutineExerciseSaveReqDto;
import com.app.bdink.workout.controller.dto.request.RoutineSetTemplateSaveReqDto;
import com.app.bdink.workout.controller.dto.response.RecommendedRoutineDetailResDto;
import com.app.bdink.workout.controller.dto.response.RecommendedRoutineSessionResDto;
import com.app.bdink.workout.controller.dto.response.RecommendedRoutineSetResDto;
import com.app.bdink.workout.entity.Exercise;
import com.app.bdink.workout.entity.RecommendedRoutine;
import com.app.bdink.workout.entity.RoutineExercise;
import com.app.bdink.workout.entity.RoutineSetTemplate;
import com.app.bdink.workout.repository.ExerciseRepository;
import com.app.bdink.workout.repository.RecommendedRoutineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendedRoutineService {

    private final RecommendedRoutineRepository routineRepo;
    private final ExerciseRepository exerciseRepository;

    public RecommendedRoutine findById(Long id) {
        return routineRepo.findById(id).orElseThrow(
                () -> new CustomException(com.app.bdink.global.exception.Error.NOT_FOUND_RECOMMENDED_EXERCISE, Error.NOT_FOUND_RECOMMENDED_EXERCISE.getMessage())
        );
    }

    // CreateRoutine
    public Long createRoutine(RecommendedRoutineSaveReqDto dto, String thumbnailUrl) {

        RecommendedRoutine routine = routineRepo.save(
                RecommendedRoutine.builder()
                        .title(dto.title())
                        .description(dto.description())
                        .thumbnailUrl(thumbnailUrl)
                        .build()
        );

        buildRoutineExercises(routine, dto.sessions());

        routineRepo.save(routine);
        return routine.getId();
    }

    // UPDATE (전체 갈아끼우기 방식)
    public void updateRoutine(Long routineId, RecommendedRoutineSaveReqDto dto) {
        RecommendedRoutine routine = routineRepo.findById(routineId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_ROUTINE, Error.NOT_FOUND_ROUTINE.getMessage()));

        routine.clearExercises(); // 기존에 저장되어있던 운동 및 세트 삭제

        routineRepo.flush();

        routine = new RecommendedRoutine(routine.getTitle(), routine.getDescription(), routine.getThumbnailUrl());

        buildRoutineExercises(routine, dto.sessions());
    }

    // HARD DELETE
    public void deleteRoutine(Long routineId) {
        RecommendedRoutine routine = routineRepo.findById(routineId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_ROUTINE, Error.NOT_FOUND_ROUTINE.getMessage()));
        routineRepo.delete(routine); // cascade로 자식 삭제
    }

    // DETAIL (추천 운동 카드 눌렀을 때)
    @Transactional(readOnly = true)
    public RecommendedRoutineDetailResDto getRoutineDetail(Long routineId) {
        RecommendedRoutine routine = routineRepo.findById(routineId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_ROUTINE, Error.NOT_FOUND_ROUTINE.getMessage()));

        List<RecommendedRoutineSessionResDto> sessions = routine.getExercises().stream()
                //orderIndex를 기준으로 루틴을 정렬
                .sorted(Comparator.comparingInt(RoutineExercise::getOrderIndex))
                .map(re -> {
                    Exercise ex = re.getExercise();
                    List<RecommendedRoutineSetResDto> sets = re.getSets().stream()
                            .sorted(Comparator.comparingInt(RoutineSetTemplate::getSetNumber))
                            .map(s -> new RecommendedRoutineSetResDto(
                                    s.getSetNumber(),
                                    s.getReps(),
                                    s.getWeight()
                            )).toList();

                    return new RecommendedRoutineSessionResDto(
                            ex.getId(),
                            ex.getName(),
                            ex.getPictureUrl(),
                            sets
                    );
                })
                .toList();

        return new RecommendedRoutineDetailResDto(routine.getTitle(), sessions);
    }

    // 내부 빌더 로직
    private void buildRoutineExercises(RecommendedRoutine routine,
                                       List<RoutineExerciseSaveReqDto> routineExerciseSaveReqDtos) {
        if (routineExerciseSaveReqDtos == null) return;

        for (RoutineExerciseSaveReqDto eDto : routineExerciseSaveReqDtos) {
            Exercise exercise = exerciseRepository.findById(eDto.exerciseId())
                    .orElseThrow(() -> new CustomException(Error.NOT_FOUND_EXERCISE, Error.NOT_FOUND_EXERCISE.getMessage()));

            RoutineExercise routineExercise = RoutineExercise.builder()
                    .exercise(exercise)
                    .memo(eDto.memo())
                    .orderIndex(eDto.orderIndex())
                    .build();

            routine.addExercise(routineExercise);

            if (eDto.sets() != null) {
                for (RoutineSetTemplateSaveReqDto sDto : eDto.sets()) {
                    RoutineSetTemplate set = RoutineSetTemplate.builder()
                            .setNumber(sDto.setNumber())
                            .reps(sDto.reps())
                            .weight(sDto.weight())
                            .build();
                    routineExercise.addSet(set);
                }
            }
        }
    }

}
