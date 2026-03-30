package com.app.bdink.subscription.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerSubscriptionScheduler {

    private final TrainerSubscriptionService trainerSubscriptionService;

    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void processSubscriptions() {
        LocalDate today = LocalDate.now();

        // 자동결제 대상 조회 및 처리
        trainerSubscriptionService.processRecurringBilling(today);
        // 만료일이 지난 구독 상태 정리
        trainerSubscriptionService.expireDueSubscriptions(today);

        log.info("Trainer subscription scheduler executed. date={}", today);
    }
}
