package com.app.bdink.external.kollus.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KollusResponse {

    private Data data;
    private String status;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private String user_key; // kollus api 응답 파싱
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private String message;
        private int code;
    }
}
