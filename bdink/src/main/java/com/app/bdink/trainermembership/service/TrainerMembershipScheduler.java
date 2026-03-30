package com.app.bdink.trainermembership.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerMembershipScheduler {

    private final TrainerMembershipService trainerMembershipService;

    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void processMemberships() {
        LocalDate today = LocalDate.now();

        // 자동결제 대상 조회 및 처리
        trainerMembershipService.processRecurringMembershipBilling(today);
        // 만료일이 지난 구독 상태 정리
        trainerMembershipService.expireDueMemberships(today);

        log.info("Trainer membership scheduler executed. date={}", today);
    }
}
