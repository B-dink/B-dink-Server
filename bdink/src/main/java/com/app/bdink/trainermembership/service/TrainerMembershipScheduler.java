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

        // 만료일이 지난 멤버십 상태만 정리한다.
        trainerMembershipService.expireDueMemberships(today);

        log.info("Trainer membership scheduler executed. date={}", today);
    }
}
