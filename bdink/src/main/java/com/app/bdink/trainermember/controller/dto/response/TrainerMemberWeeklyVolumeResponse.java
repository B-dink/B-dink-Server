package com.app.bdink.trainermember.controller.dto.response;

/**
 * 트레이너가 관리 중인 멤버의 주간 볼륨 차이
 */
public record TrainerMemberWeeklyVolumeResponse(
        Long memberId,
        String memberName,
        long weeklyVolumeDelta,
        boolean weeklyVolumeIncreased
) {
}
