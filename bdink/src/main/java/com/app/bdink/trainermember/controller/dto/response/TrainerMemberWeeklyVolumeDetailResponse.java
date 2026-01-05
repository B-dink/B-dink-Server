package com.app.bdink.trainermember.controller.dto.response;

import java.util.List;

/**
 * 회원의 주간 일별 볼륨 데이터 응답 DTO.
 */
public record TrainerMemberWeeklyVolumeDetailResponse(
        Long memberId,
        String trainerMemberCreatedDate,
        List<DailyVolume> dailyVolumes
) {
    /**
     * 일자별 볼륨 응답 DTO.
     */
    public record DailyVolume(
            String date,
            long volume
    ) {
    }
}
