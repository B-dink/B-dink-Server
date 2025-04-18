package com.app.bdink.external.kollus.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import javax.xml.transform.Result;

public class KollusResponse {

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KollusUserKeyResponse {
        private Result result;
        private String user_key;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private String message;
        private int code;
    }
}
