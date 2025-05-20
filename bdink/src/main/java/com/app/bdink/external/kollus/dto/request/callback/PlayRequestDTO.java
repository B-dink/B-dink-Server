package com.app.bdink.external.kollus.dto.request.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;
@Data
public class PlayRequestDTO {
        private Integer kind;

        @JsonProperty("client_user_id")
        private String clientUserId;

        @JsonProperty("player_id")
        private String playerId;

        @JsonProperty("device_name")
        private String deviceName;

        @JsonProperty("media_content_key")
        private String mediaContentKey;

        private Map<String, String> uservalues;
}

