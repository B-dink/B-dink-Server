package com.app.bdink.workout.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import com.app.bdink.workout.controller.dto.ExercisePart;
import com.app.bdink.workout.controller.dto.request.ExerciseReqDto;
import com.app.bdink.workout.controller.dto.request.PerformedExerciseSaveReqDto;
import com.app.bdink.workout.controller.dto.request.WorkoutSessionSaveReqDto;
import com.app.bdink.workout.controller.dto.request.WorkoutSetSaveReqDto;
import com.app.bdink.workout.controller.dto.response.ExerciseResDto;
import com.app.bdink.workout.entity.Exercise;
import com.app.bdink.workout.entity.PerformedExercise;
import com.app.bdink.workout.entity.WorkOutSession;
import com.app.bdink.workout.entity.WorkoutSet;
import com.app.bdink.workout.repository.ExerciseRepository;
import com.app.bdink.workout.repository.PerformedExerciseRepository;
import com.app.bdink.workout.repository.WorkOutSessionRepository;
import com.app.bdink.workout.repository.WorkoutSetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.SessionIdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkoutService {
    private final ExerciseRepository exerciseRepository;
    private final PerformedExerciseRepository performedExerciseRepository;
    private final WorkOutSessionRepository workOutSessionRepository;
    private final WorkoutSetRepository workoutSetRepository;


    public Exercise findById(Long id) {
        return exerciseRepository.findById(id).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_EXERCISE, Error.NOT_FOUND_EXERCISE.getMessage())
        );
    }

    public WorkOutSession findWorkoutSession(Long id, Member member) {
         return workOutSessionRepository.findByIdAndMember(id, member).orElseThrow(
                () -> new CustomException(Error.NOT_FOUND_WORKOUTSESSION, Error.NOT_FOUND_WORKOUTSESSION.getMessage())
        );
    }

    public String createExercise(ExerciseReqDto exerciseReqDto,
                                 String exerciseVideoUrl,
                                 String exercisePictureUrl
                                 ) {
        Exercise exercise = exerciseRepository.save(
                Exercise.builder()
                        .name(exerciseReqDto.ExerciseName())
                        .description(exerciseReqDto.ExerciseDescription())
                        .videoUrl(exerciseVideoUrl)
                        .pictureUrl(exercisePictureUrl)
                        .part(exerciseReqDto.ExercisePart())
                        .build()
        );
        return String.valueOf(exercise.getId());
    }

    @Transactional(readOnly = true)
    public List<ExerciseResDto> getPart(ExercisePart exercisePart) {
        List<Exercise> exercises = exerciseRepository.findAllByPart(exercisePart);

        return exercises.stream()
                .map(ExerciseResDto::of)
                .toList();
    }

    @Transactional
    public ExerciseResDto updateExerciseInfo(
            final ExerciseReqDto exerciseReqDto,
            final String videoUrlKey,
            final String pictureUrlKey,
            final Long exerciseId
    ){
        Exercise exercise = findById(exerciseId);
        exercise.modifyExercise(exerciseReqDto, videoUrlKey, pictureUrlKey);

        return ExerciseResDto.of(exercise);
    }

    //기록완료 버튼을 눌렀을 때 호출되는 메서드
    @Transactional
    public String saveWorkoutSession(Member member, WorkoutSessionSaveReqDto requestDto){
        /***
         * 1. WorkoutSession 생성 (운동일지)
         * 2. PerformedExercise + WorkoutSession 생성 (수행한 운동, 수행한 세트)
         */
        WorkOutSession session = workOutSessionRepository.save(
                WorkOutSession.builder()
                        .memo(requestDto.memo())
                        .member(member)
                        .build()
        );

        for (PerformedExerciseSaveReqDto exerciseDto : requestDto.performedExercises()) {

            Exercise exercise = findById(exerciseDto.exerciseId());

            PerformedExercise performedExercise = performedExerciseRepository.save(
                    PerformedExercise.builder()
                            .exercise(exercise)
                            .workOutSession(session)
                            .build()
            );

            for (WorkoutSetSaveReqDto setDto : exerciseDto.sets()){

                WorkoutSet workoutSet = workoutSetRepository.save(
                        WorkoutSet.builder()
                                .performedExercise(performedExercise)
                                .setNumber(setDto.setNumber())
                                .weight(setDto.weight())
                                .reps(setDto.reps())
//                                .restTime(setDto.restTime())
                                .build()
                );
            }

        }
        return String.valueOf(session.getId());
    }

    // 운동 기록 삭제 메서드
    @Transactional
    public void deleteWorkoutSession(Member member, Long sessionId){
        WorkOutSession session = findWorkoutSession(sessionId, member);
        workOutSessionRepository.delete(session);
    }
}
