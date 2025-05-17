package com.app.bdink.external.kollus.dto.request.callback;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record PlayRequestDTO (
        Integer kind,

        @JsonProperty("client_user_id")
        String clientUserId,

        @JsonProperty("player_id")
        String playerId,

        @JsonProperty("device_name")
        String deviceName,

        @JsonProperty("media_content_key")
        String mediaContentKey,

        Map<String, String> uservalues
)
{
}
