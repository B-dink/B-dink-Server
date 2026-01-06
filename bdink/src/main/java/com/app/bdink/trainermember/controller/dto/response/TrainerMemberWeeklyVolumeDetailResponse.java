package com.app.bdink.trainermember.controller.dto.response;

import com.app.bdink.workout.controller.dto.ExercisePart;

import java.util.List;
import java.util.Map;

/**
 * 회원의 주간 일별 볼륨 데이터 응답 DTO.
 * - dailyVolumes: 월~일 7일의 일별 볼륨 합계
 * - partScores: 주간 부위별 점수/레벨 합계 (main:2점, sub:1점, OTHERS 제외)
 */
public record TrainerMemberWeeklyVolumeDetailResponse(
        Long memberId,
        String trainerMemberCreatedDate,
        String yearMonthWeekLabel,
        List<DailyVolume> dailyVolumes,
        Map<ExercisePart, PartScore> partScores
) {
    /**
     * 일자별 볼륨 응답 DTO.
     */
    public record DailyVolume(
            String date,
            long volume
    ) {
    }

    /**
     * 부위별 점수/레벨 응답 DTO.
     */
    public record PartScore(
            int score,
            int level
    ) {
    }
}
