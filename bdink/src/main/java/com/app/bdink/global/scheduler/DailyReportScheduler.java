package com.app.bdink.global.scheduler;

import com.app.bdink.global.discord.service.DiscordWebhookService;
import com.app.bdink.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DailyReportScheduler {

    private final MemberRepository memberRepository;
    private final DiscordWebhookService discordWebhookService;

    @Scheduled(cron = "0 0 9 * * *") // 매일 오전 9시
    public void sendDailySignupReport() {

        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime start = yesterday.atStartOfDay();
        LocalDateTime end = yesterday.plusDays(1).atStartOfDay();

        long dailySignup = memberRepository.countDailySignup(start, end);
        long totalMember = memberRepository.countTotalMember();

        String message = String.format("""
            📊 [일일 회원 리포트 - %s 기준]

            👥 어제 신규 회원 가입수 : %d명  
            📦 현재 누적 회원 수 : %d명  

            > 📌 매일 오전 9시에 자동 전송됩니다.
            """, yesterday, dailySignup, totalMember);

        discordWebhookService.sendMessage(message);
    }
}
