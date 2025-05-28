package com.app.bdink.global.totalTime;

import java.time.Duration;

public class TotalTimeUtil {
    public static String formatSecondsToHHMMSS(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static Duration toDuration(long totalSeconds) {
        return Duration.ofSeconds(totalSeconds);
    }
}
