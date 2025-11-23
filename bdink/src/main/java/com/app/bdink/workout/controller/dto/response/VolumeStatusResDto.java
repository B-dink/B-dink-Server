package com.app.bdink.workout.controller.dto.response;

public record VolumeStatusResDto(
        Integer rank,
        Long totalParticipants,
        Long weeklyVolume,
        Long todayVolume
) {
}
