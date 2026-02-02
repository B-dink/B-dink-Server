package com.app.bdink.member.service;

import com.app.bdink.member.entity.Member;
import com.app.bdink.member.entity.MemberStatus;
import com.app.bdink.member.repository.MemberRepository;
import com.app.bdink.notification.service.DeviceTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberDormantScheduler {

    private final MemberRepository memberRepository;
    private final DeviceTokenService deviceTokenService;

    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void markDormantMembers() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);
        List<Member> members = memberRepository.findAllByStatusAndLastLoginAtBefore(MemberStatus.ACTIVE, threshold);
        for (Member member : members) {
            member.dormancy();
            deviceTokenService.deactivateAllForMember(member.getId());
        }
        if (!members.isEmpty()) {
            log.info("Dormant members updated: {}", members.size());
        }
    }
}
