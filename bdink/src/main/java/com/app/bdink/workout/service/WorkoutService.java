package com.app.bdink.workout.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import com.app.bdink.openai.dto.request.AiWorkoutMemoReqDto;
import com.app.bdink.openai.parser.AiParsedExerciseDto;
import com.app.bdink.openai.parser.AiParsedWorkoutDto;
import com.app.bdink.openai.service.OpenAiChatService;
import com.app.bdink.workout.controller.dto.ExercisePart;
import com.app.bdink.workout.controller.dto.MemberWeeklyVolumeDto;
import com.app.bdink.workout.controller.dto.request.ExerciseReqDto;
import com.app.bdink.workout.controller.dto.request.PerformedExerciseSaveReqDto;
import com.app.bdink.workout.controller.dto.request.WorkoutSessionSaveReqDto;
import com.app.bdink.workout.controller.dto.request.WorkoutSetSaveReqDto;
import com.app.bdink.workout.controller.dto.response.*;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkoutService {
    private final ExerciseRepository exerciseRepository;
    private final PerformedExerciseRepository performedExerciseRepository;
    private final WorkOutSessionRepository workOutSessionRepository;
    private final WorkoutSetRepository workoutSetRepository;
    private final OpenAiChatService openAiChatService;


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

    @Transactional(readOnly = true)
    public ExerciseResDto getExerciseById(Long exerciseId) {
        Exercise exercise = findById(exerciseId);
        return ExerciseResDto.of(exercise);
    }

    @Transactional
    public ExerciseResDto updateExerciseInfo(
            final ExerciseReqDto exerciseReqDto,
            final String videoUrlKey,
            final String pictureUrlKey,
            final Long exerciseId
    ) {
        Exercise exercise = findById(exerciseId);
        exercise.modifyExercise(exerciseReqDto, videoUrlKey, pictureUrlKey);

        return ExerciseResDto.of(exercise);
    }

    //기록완료 버튼을 눌렀을 때 호출되는 메서드
    @Transactional
    public String saveWorkoutSession(Member member, WorkoutSessionSaveReqDto requestDto) {
        /***
         * 1. WorkoutSession 생성 (운동일지)
         * 2. PerformedExercise + WorkoutSession 생성 (수행한 운동, 수행한 세트)
         */
        WorkOutSession session = workOutSessionRepository.save(
                WorkOutSession.builder()
                        .memo(requestDto.todayWorkoutName())
                        .workoutMemo(requestDto.workoutMemo())
                        .member(member)
                        .build()
        );

        for (PerformedExerciseSaveReqDto exerciseDto : requestDto.performedExercises()) {

            Exercise exercise = findById(exerciseDto.exerciseId());

            PerformedExercise performedExercise = performedExerciseRepository.save(
                    PerformedExercise.builder()
                            .exercise(exercise)
                            .workOutSession(session)
                            .memo(exerciseDto.memo())
                            .build()
            );

            for (WorkoutSetSaveReqDto setDto : exerciseDto.sets()) {

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
    public void deleteWorkoutSession(Member member, Long sessionId) {
        WorkOutSession session = findWorkoutSession(sessionId, member);
        workOutSessionRepository.delete(session);
    }

    // 운동 기록 수정 메서드
    @Transactional
    public String updateWorkoutSession(Member member, Long sessionId, WorkoutSessionSaveReqDto requestDto) {
        WorkOutSession session = findWorkoutSession(sessionId, member);

        /***
         * 1. memo 수정
         * 2. 기존 performedExercise + 세트 전부 삭제 (orphanRemoval)
         * 3. 새로운 데이터로 다시 구성
         */

        session.changeMemo(requestDto.todayWorkoutName());

        session.changeWorkoutMemo(requestDto.workoutMemo());

        session.clearPerformedExercises();

        for (PerformedExerciseSaveReqDto exerciseDto : requestDto.performedExercises()) {
            Exercise exercise = findById(exerciseDto.exerciseId());

            PerformedExercise performedExercise = PerformedExercise.builder()
                    .workOutSession(session)
                    .exercise(exercise)
                    .memo(exerciseDto.memo())
                    .build();

            session.addPerformedExercise(performedExercise);


            for (WorkoutSetSaveReqDto setDto : exerciseDto.sets()) {

                WorkoutSet workoutSet = WorkoutSet.builder()
                        .performedExercise(performedExercise)
                        .setNumber(setDto.setNumber())
                        .weight(setDto.weight())
                        .reps(setDto.reps())
                        .build();

                performedExercise.addWorkoutSet(workoutSet);
            }
        }
        return String.valueOf(session.getId());
    }

    // 월 별 운동일자 기록 조회
    @Transactional(readOnly = true)
    public WorkoutCalendarResDto getWorkoutCalender(Member member, int year, int month) {

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate firstDayNextMonth = firstDay.plusMonths(1);

        LocalDateTime start = firstDay.atStartOfDay();
        LocalDateTime end = firstDayNextMonth.atStartOfDay();

        List<WorkOutSession> sessions =
                workOutSessionRepository.findByMemberAndCreatedAtBetween(member, start, end);

        // createdAt 기준 -> 날짜 추출, 중복 제거 및 정렬
        List<Integer> days = sessions.stream()
                .map(s -> s.getCreatedAt().toLocalDate().getDayOfMonth())
                .distinct()
                .sorted()
                .toList();

        return new WorkoutCalendarResDto(year, month, days);
    }

    // 볼륨 현황 계산
    @Transactional(readOnly = true)
    public VolumeStatusResDto getVolumeStatus(Member member, LocalDate baseDate) {

        // 1. 이번 주(Mon~Sun) 계산
        LocalDate thisWeekStart = baseDate.with(DayOfWeek.MONDAY);
        LocalDate thisWeekEnd = thisWeekStart.plusDays(6);

        LocalDateTime weekStart = thisWeekStart.atStartOfDay();
        LocalDateTime weekEnd = thisWeekEnd.atStartOfDay();

        // 2. 오늘 날짜 범위
        LocalDateTime todayStart = baseDate.atStartOfDay();
        LocalDateTime todayEnd = baseDate.plusDays(1).atStartOfDay();

        // 3. 주간 랭킹 리스트 조회
        List<MemberWeeklyVolumeDto> ranking =
                workoutSetRepository.findWeeklyVolumeRanking(weekStart, weekEnd);

        //TODO: 총 참여자를 잡을 때 전체 유저 수로 변경될 가능성 존재
        long totalParticipants = ranking.size();

        // 4. 내 순위 + 내 주간 볼륨
        //TODO : 기획적으로 랭킹에 없을 경우 0 or 전체 참여자 수 + 1 택
        int rank = 0;
        long myWeeklyVolume = 0;

        for (int i = 0; i < ranking.size(); i++) {
            MemberWeeklyVolumeDto dto = ranking.get(i);
            if (dto.memberId().equals(member.getId())) {
                rank = i + 1;                 // 리스트는 0부터, 랭킹은 1부터
                myWeeklyVolume = dto.weeklyVolume();
                break;
            }
        }

        // 5. 오늘 볼륨 계산로직
        Long todayVolume = workoutSetRepository.calculateVolumeForPeriod(
                member, todayStart, todayEnd
        );
        if (todayVolume == null) {
            todayVolume = 0L;
        }

        return new VolumeStatusResDto(
                rank,
                totalParticipants,
                myWeeklyVolume,
                todayVolume
        );
    }

    // 이번 주, 지난 주 볼륨 그래프 데이터 조회
    @Transactional(readOnly = true)
    public WeeklyVolumeGraphResDto getWeeklyVolumeGraph(Member member, LocalDate baseDate) {
        LocalDate thisWeekStart = baseDate.with(DayOfWeek.MONDAY);
        LocalDate lastWeekStart = thisWeekStart.minusWeeks(1);

        List<Long> thisWeekVolumes = new ArrayList<>();
        List<Long> lastWeekVolumes = new ArrayList<>();

        // 2) 월~일 7일 동안 각각 볼륨 계산
        for (int i = 0; i < 7; i++) {
            // 이번 주 i일째 (월+0, 화+1, ...)
            LocalDate thisDay = thisWeekStart.plusDays(i);
            LocalDateTime thisStart = thisDay.atStartOfDay();
            LocalDateTime thisEnd = thisDay.plusDays(1).atStartOfDay();

            Long thisVolume = workoutSetRepository.calculateVolumeForPeriod(
                    member, thisStart, thisEnd
            );
            if (thisVolume == null) thisVolume = 0L;
            thisWeekVolumes.add(thisVolume);

            // 지난 주 i일째
            LocalDate lastDay = lastWeekStart.plusDays(i);
            LocalDateTime lastStart = lastDay.atStartOfDay();
            LocalDateTime lastEnd = lastDay.plusDays(1).atStartOfDay();

            Long lastVolume = workoutSetRepository.calculateVolumeForPeriod(
                    member, lastStart, lastEnd
            );
            if (lastVolume == null) lastVolume = 0L;
            lastWeekVolumes.add(lastVolume);
        }

        return new WeeklyVolumeGraphResDto(
                thisWeekStart,
                lastWeekStart,
                thisWeekVolumes,
                lastWeekVolumes
        );
    }

    @Transactional(readOnly = true)
    public List<ExerciseSearchResDto> searchExercises(String keyword, ExercisePart part) {

        if (keyword == null || keyword.isBlank()) {
            throw new CustomException(Error.INVALID_SEARCH_EMPTY_EXCEPTION, Error.INVALID_SEARCH_EMPTY_EXCEPTION.getMessage());
        }

        List<Exercise> exercises =
                exerciseRepository.findByPartAndNameContainingIgnoreCase(part, keyword);

        return exercises.stream()
                .map(ExerciseSearchResDto::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public WorkoutDailyDetailResDto getWorkoutDailyDetail(Member member, LocalDate date) {

        //시작일자 부터 다음날짜 사이의 모든 운동일지를 조회
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<WorkOutSession> sessions = workOutSessionRepository
                .findByMemberAndCreatedAtBetween(member, start, end);

        if (sessions.isEmpty()) {
            throw new CustomException(Error.NOT_FOUND_WORKOUTSESSION, Error.NOT_FOUND_WORKOUTSESSION.getMessage());
        }

        // 하루에 한 개의 운동일지만 작성 가능.
        WorkOutSession session = sessions.get(0);

        String dateString = session.getCreatedAt().toLocalDate().toString();
        String workoutName = session.getMemo();
        String workoutMemo = session.getWorkoutMemo();

        List<WorkoutDailyExerciseResDto> exercises = session.getPerformedExercises().stream()
                .map(pe ->
                        {
                            Exercise exercise = pe.getExercise();

                            List<WorkoutDailySetResDto> sets = pe.getWorkoutSets().stream()
                                    .map(ws -> new WorkoutDailySetResDto(
                                            ws.getSetNumber(),
                                            ws.getReps(),
                                            ws.getWeight()))
                                    .toList();
                            return new WorkoutDailyExerciseResDto(
                                    exercise.getId(),
                                    exercise.getName(),
                                    exercise.getPictureUrl(),
                                    sets
                            );
                        }

                )
                .toList();

        return new WorkoutDailyDetailResDto(
                dateString,
                workoutName,
                workoutMemo,
                session.getId(),
                exercises
        );

    }

    //지난주 볼륨 비교 및 지난달 운동 횟수 비교 로직
    @Transactional(readOnly = true)
    public WeeklyVolumeCompareResDto getWeeklyVolumeCompare(Member member, LocalDate baseDate) {
        // 1. 이번 주(Mon~Sun) 계산
        LocalDate thisWeekStart = baseDate.with(DayOfWeek.MONDAY);
        LocalDate thisWeekEnd = thisWeekStart.plusDays(6);

        // 2. 지난 주(Mon~Sun) 계산
        LocalDate lastWeekStart = thisWeekStart.minusWeeks(1);
        LocalDate lastWeekEnd = thisWeekStart.minusDays(1);

        long thisWeekVolume = calcWeekVolume(member, thisWeekStart, thisWeekEnd);
        long lastWeekVolume = calcWeekVolume(member, lastWeekStart, lastWeekEnd);

        long diffVolume = thisWeekVolume - lastWeekVolume;

        double percent;

        if (lastWeekVolume == 0) {
            percent = (thisWeekVolume == 0) ? 0 : 100;
        } else {
            percent = (thisWeekVolume - lastWeekVolume) * 100.0 / lastWeekVolume;
        }

        //절대값으로 계산
        int percentRounded = (int) Math.round(Math.abs(percent));
        // 지난 주 보다 향상 > true, 하락 or 동일 false
        boolean increased = diffVolume > 0;

        // 월간 운동 횟수 비교 계산
        MonthlyWorkoutCountCompareResDto monDto = calcMonthlyWorkoutCompare(member, baseDate);

        return new WeeklyVolumeCompareResDto(
                lastWeekVolume,
                thisWeekVolume,
                diffVolume,
                percentRounded,
                increased,
                monDto
        );

    }

    @Transactional(readOnly = true)
    public List<ExerciseResDto> getExerciseList() {
        return exerciseRepository.findAll().stream()
                .map(ExerciseResDto::of)
                .toList();
    }

    // LLM memoText 변환 로직
    public AiMemoResDto convertMemoTextToRequestDto(AiWorkoutMemoReqDto dto) {

        // 1) LLM 파싱 로직 memeText -> 운동, 세트 정보
        AiParsedWorkoutDto parsed = openAiChatService.parsedWorkoutDtoDto(dto.memoText());

        // 2) exerciseName 기준으로 DB Exercise 찾기 (초기모델, LIKE -> first)
        List<WorkoutDailyExerciseResDto> workoutExercises = parsed.exercises().stream()
                .map(this::mapToExerciseResDto)
                .filter(Objects::nonNull)
                .toList();

        // 3) workoutName custom
        String todayWorkoutName = LocalDate.now(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd 운동일지"));

        // 4) workoutMemo is null
        return new AiMemoResDto(
                todayWorkoutName,
                null,
                workoutExercises
        );

    }

    private WorkoutDailyExerciseResDto mapToExerciseResDto(AiParsedExerciseDto dto){
        Exercise exercise = findFirstSimilarOrNull(dto.exerciseName());

        if (exercise == null) return null;

        return new WorkoutDailyExerciseResDto(
                exercise.getId(),
                exercise.getName(),
                exercise.getPictureUrl(),
                //TODO: 이쪽 리스토로 출력이 안되면 다시 확인하기
                dto.sets()
        );
    }

    // LLM을 위한 프로토타입 로직 (ai 메모장)
    public Exercise findFirstSimilarOrNull(String rawName){
        if(rawName == null || rawName.isBlank()) return null;

        String keyword = rawName.trim();

        List<Exercise> likeExercises = exerciseRepository.findByNameContainingIgnoreCase(keyword);

        // 매칭이 안될 경우 null
        if(likeExercises.isEmpty()) return null;

        // mvp 단계이기 때문에 여러개 중 한 개만 사용
        return likeExercises.get(0);
    }


    @Transactional(readOnly = true)
    public ExerciseVersionResDto getExerciseFromVersion(String version){

        LocalDate clientVersionDate = LocalDate.parse(version);

        Optional<LocalDate> optionalLastDate = exerciseRepository.findLastCreatedAtDate();

        // 서버에 Data가 없는 경우
        if (optionalLastDate.isEmpty()) {
            // 서버 기준 최신 버전도 없으니 클라 버전 그대로 돌려주고, 업데이트 없음
            return new ExerciseVersionResDto(
                    version,
                    false,   // 변경된 운동 없음
                    List.of()
            );
        }

        LocalDate lastCreatedDate = optionalLastDate.get();
        String latestVersion = lastCreatedDate.toString();

        // 클라 버전이 서버 버전보다 높은 경우
        if (!clientVersionDate.isBefore(lastCreatedDate)) {
            // clientVersionDate >= lastCreatedDate
            return new ExerciseVersionResDto(
                    latestVersion,
                    false,
                    List.of()
            );
        }

        // 클라 버전의 다음날부터(lastCreatedDate까지 포함)
        LocalDate startDate = clientVersionDate.plusDays(1);
        LocalDate endDate = lastCreatedDate;

        LocalDateTime from = startDate.atStartOfDay();
        LocalDateTime to   = endDate.plusDays(1).atStartOfDay();

        List<Exercise> exercises =
                exerciseRepository.findAllByCreatedAtBetween(from, to);

        List<ExerciseResDto> exerciseDtos = exercises.stream()
                .map(ExerciseResDto::of)
                .toList();

        return new ExerciseVersionResDto(
                latestVersion,
                true,
                exerciseDtos
        );
    }


    // 이번주 볼륨 계산 로직
    private long calcWeekVolume(Member member, LocalDate start, LocalDate end) {
        LocalDateTime from = start.atStartOfDay();
        LocalDateTime to = end.plusDays(1).atStartOfDay(); // end 포함

        List<WorkOutSession> sessions =
                workOutSessionRepository.findByMemberAndCreatedAtBetween(member, from, to);

        return sessions.stream()
                .flatMap(s -> s.getPerformedExercises().stream())
                .flatMap(pe -> pe.getWorkoutSets().stream())
                .mapToLong(ws -> (long) ws.getWeight() * ws.getReps())
                .sum();
    }

    // 이번달 운동 횟수 계산 로직
    private int calcThisMonthWorkoutCount(Member member, LocalDate baseDate) {
        LocalDate firstDayThisMonth = baseDate.withDayOfMonth(1);
        LocalDate firstDayNextMonth = firstDayThisMonth.plusMonths(1);

        LocalDateTime from = firstDayThisMonth.atStartOfDay();
        LocalDateTime to = firstDayNextMonth.atStartOfDay();

        return workOutSessionRepository.findByMemberAndCreatedAtBetween(member, from, to).size();
    }

    // 저번달 운동 횟수 계산 로직
    private int calcLastMonthWorkoutCount(Member member, LocalDate baseDate) {
        LocalDate firstDayLastMonth = baseDate.withDayOfMonth(1).minusMonths(1);
        LocalDate firstDayNextMonth = firstDayLastMonth.plusMonths(1);

        LocalDateTime from = firstDayLastMonth.atStartOfDay();
        LocalDateTime to = firstDayNextMonth.atStartOfDay();

        return workOutSessionRepository.findByMemberAndCreatedAtBetween(member, from, to).size();
    }

    // 운동 횟수 계산 로직
    private MonthlyWorkoutCountCompareResDto calcMonthlyWorkoutCompare(Member member, LocalDate baseDate) {
        // 운동 횟수 계산 로직
        int thisMonthWorkoutCount = calcThisMonthWorkoutCount(member, baseDate);
        int lastMonthWorkoutCount = calcLastMonthWorkoutCount(member, baseDate);

        int diffCount = thisMonthWorkoutCount - lastMonthWorkoutCount;

        double monthPercent;

        if (lastMonthWorkoutCount == 0) {
            // 이번달 운동 횟수가 0일 경우
            monthPercent = (thisMonthWorkoutCount == 0) ? 0 : 100;
        } else {
            // 변화율 = 변화량 * 100/저번달 운동 횟수
            monthPercent = diffCount * 100.0 / lastMonthWorkoutCount;
        }

        //변화율 절댓값
        int montPercentRounded = (int) Math.round(Math.abs(monthPercent));

        // 향상 or 감소
        boolean monthIncreased = diffCount > 0;

        // 이번달 총 일 수
        int daysInThisMonth = baseDate.withDayOfMonth(1).lengthOfMonth();

        return new MonthlyWorkoutCountCompareResDto(
                thisMonthWorkoutCount,
                lastMonthWorkoutCount,
                diffCount,
                montPercentRounded,
                monthIncreased,
                daysInThisMonth
        );
    }
}
