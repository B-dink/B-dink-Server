package com.app.bdink.global.discord.service;

import com.app.bdink.global.scheduler.DailyReportScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DiscordWebhookServiceTest {
    @Autowired
    private DailyReportScheduler dailyReportScheduler;

    @Test
    void testSendDailySignupReport() {
        dailyReportScheduler.sendDailySignupReport();
    }
}