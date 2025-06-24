package com.app.bdink.global.discord.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiscordWebhookService {
    @Value("${discord.WEBHOOK-URL}")
    private String WEBHOOK_URL;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String message) {
        Map<String, String> payload = Map.of("content", message);
        restTemplate.postForEntity(WEBHOOK_URL, payload, String.class);
    }
}
