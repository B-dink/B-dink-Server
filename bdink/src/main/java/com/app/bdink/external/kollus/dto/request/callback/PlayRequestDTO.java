package com.app.bdink.external.kollus.dto.request.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;
@Data
public class PlayRequestDTO {
        private Integer kind;

        private String client_user_id;

        private String player_id;

        private String device_name;

        private String media_content_key;

        private Map<String, String> uservalues;
}

