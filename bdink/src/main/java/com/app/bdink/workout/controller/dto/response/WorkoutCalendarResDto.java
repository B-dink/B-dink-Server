package com.app.bdink.workout.controller.dto.response;

import java.util.List;

public record WorkoutCalendarResDto(
        Integer year,
        Integer month,
        List<Integer> days //운동한 날짜들 (ex. [1, 3, 5, 10])
) {
}
