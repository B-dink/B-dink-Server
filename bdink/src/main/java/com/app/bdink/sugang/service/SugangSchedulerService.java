package com.app.bdink.sugang.service;

import com.app.bdink.sugang.controller.dto.SugangStatus;
import com.app.bdink.sugang.entity.Sugang;
import com.app.bdink.sugang.repository.SugangRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SugangSchedulerService {

    private final SugangRepository sugangRepository;

    @Transactional
    @Scheduled(cron = "0 0 3 * * *") // 매일 새벽 3시
    public void expireSugangStatus() {
        LocalDate now = LocalDate.now();
        List<Sugang> expiredSugangList = sugangRepository
                .findByExpiredDateBeforeAndSugangStatus(now, SugangStatus.PAYMENT_COMPLETED);

        for (Sugang sugang : expiredSugangList) {
            sugang.updateSugangStatus(SugangStatus.EXPIRED);
        }
    }
}
