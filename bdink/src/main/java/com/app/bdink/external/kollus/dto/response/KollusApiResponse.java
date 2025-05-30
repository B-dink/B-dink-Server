package com.app.bdink.external.kollus.dto.response;

import lombok.*;

public class KollusApiResponse {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KollusUrlResponse {
        private String url;
        private String lectureTitle;
        private String InstructorName;
        private Long prevLectureId;
        private Long nextLectureId;
    }
}
