package com.app.bdink.subscription.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import com.app.bdink.subscription.entity.SubscriptionPlan;
import com.app.bdink.subscription.entity.TrainerMembershipStatus;
import com.app.bdink.subscription.entity.TrainerSubscription;
import com.app.bdink.subscription.repository.SubscriptionPlanRepository;
import com.app.bdink.subscription.repository.TrainerMembershipRepository;
import com.app.bdink.trainer.entity.Trainer;
import com.app.bdink.trainer.service.TrainerQrService;
import com.app.bdink.trainer.service.TrainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerMembershipService {

    private final TrainerMembershipRepository trainerMembershipRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final TrainerService trainerService;
    private final TrainerQrService trainerQrService;

    @Transactional(readOnly = true)
    public TrainerSubscription findById(Long id) {
        return trainerMembershipRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND, Error.NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    public SubscriptionPlan findPlanById(Long planId) {
        return subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND, Error.NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    public List<SubscriptionPlan> getActivePlans() {
        return subscriptionPlanRepository.findAllByActiveTrue();
    }

    @Transactional(readOnly = true)
    public TrainerSubscription getActiveMembership(Long trainerId) {
        Trainer trainer = trainerService.getActiveTrainer(trainerId);
        return trainerMembershipRepository.findByTrainerAndSubscriptionStatus(trainer, TrainerMembershipStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND, Error.NOT_FOUND.getMessage()));
    }

    @Transactional
    public TrainerSubscription createMembership(Long trainerId, Long subscriptionPlanId,
                                                LocalDate paymentDate, boolean autoRenew) {
        Trainer trainer = trainerService.getActiveTrainer(trainerId);
        SubscriptionPlan subscriptionPlan = findPlanById(subscriptionPlanId);

        trainerMembershipRepository.findByTrainerAndSubscriptionStatus(trainer, TrainerMembershipStatus.ACTIVE)
                .ifPresent(subscription -> {
                    throw new CustomException(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage());
                });

        TrainerSubscription subscription = TrainerSubscription.create(trainer, subscriptionPlan, paymentDate, autoRenew);
        return trainerMembershipRepository.save(subscription);
    }

    @Transactional
    public TrainerSubscription createMembershipForMember(Member member, Long subscriptionPlanId,
                                                         LocalDate paymentDate, boolean autoRenew) {
        Trainer trainer = trainerService.getOrCreatePaidTrainer(member);
        trainerQrService.ensureTrainerQr(trainer);
        SubscriptionPlan subscriptionPlan = findPlanById(subscriptionPlanId);

        trainerMembershipRepository.findByTrainerAndSubscriptionStatus(trainer, TrainerMembershipStatus.ACTIVE)
                .ifPresent(subscription -> {
                    throw new CustomException(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage());
                });

        TrainerSubscription subscription = TrainerSubscription.create(trainer, subscriptionPlan, paymentDate, autoRenew);
        return trainerMembershipRepository.save(subscription);
    }

    @Transactional
    public TrainerSubscription renewMembership(Long membershipId, LocalDate paymentDate) {
        TrainerSubscription subscription = findById(membershipId);
        subscription.renew(paymentDate);
        return subscription;
    }

    @Transactional
    public TrainerSubscription cancelMembership(Long membershipId, LocalDate canceledDate) {
        TrainerSubscription subscription = findById(membershipId);
        subscription.cancel(canceledDate);
        return subscription;
    }

    @Transactional
    public TrainerSubscription expireMembership(Long membershipId, LocalDate expiredDate) {
        TrainerSubscription subscription = findById(membershipId);
        subscription.expire(expiredDate);
        return subscription;
    }

    @Transactional
    public TrainerSubscription markMembershipPaymentFailed(Long membershipId) {
        TrainerSubscription subscription = findById(membershipId);
        subscription.markPaymentFailed();
        return subscription;
    }

    @Transactional(readOnly = true)
    public List<TrainerSubscription> getBillingDueMemberships(LocalDate date) {
        return trainerMembershipRepository.findAllBySubscriptionStatusAndAutoRenewTrueAndNextBillingDateLessThanEqual(
                TrainerMembershipStatus.ACTIVE, date
        );
    }

    @Transactional(readOnly = true)
    public List<TrainerSubscription> getExpiredMemberships(LocalDate date) {
        return trainerMembershipRepository.findAllByExpiredDateBeforeAndSubscriptionStatus(
                date, TrainerMembershipStatus.ACTIVE
        );
    }

    @Transactional
    public void processRecurringMembershipBilling(LocalDate today) {
        List<TrainerSubscription> dueMemberships = getBillingDueMemberships(today);

        // 실제 정기결제 연동 전까지는 청구 대상만 로그로 남긴다.
        for (TrainerSubscription membership : dueMemberships) {
            log.info("Recurring membership billing due. membershipId={}, trainerId={}, nextBillingDate={}",
                    membership.getId(), membership.getTrainer().getId(), membership.getNextBillingDate());
        }
    }

    @Transactional
    public void expireDueMemberships(LocalDate today) {
        List<TrainerSubscription> expiredMemberships = getExpiredMemberships(today);

        for (TrainerSubscription membership : expiredMemberships) {
            membership.expire(membership.getExpiredDate());
        }

        if (!expiredMemberships.isEmpty()) {
            log.info("Expired trainer memberships updated: {}", expiredMemberships.size());
        }
    }
}
