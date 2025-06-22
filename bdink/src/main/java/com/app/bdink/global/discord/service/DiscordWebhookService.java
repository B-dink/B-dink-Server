package com.app.bdink.global.discord.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiscordWebhookService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String webhookUrl = "https://discord.com/api/webhooks/1386490380354060298/q7vtDsZ6NhuHxor13Vukk_tHitQPf8Db8M2fcGg1ln8-u802QMHXXYKGD9atn4F4WMUp"; //Todo: 현재 테스트 url, 실제 URL로 교체 필요

    public void sendMessage(String message) {
        Map<String, String> payload = Map.of("content", message);
        restTemplate.postForEntity(webhookUrl, payload, String.class);
    }
}
