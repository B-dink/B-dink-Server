package com.app.bdink.subscription.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import com.app.bdink.subscription.entity.SubscriptionPlan;
import com.app.bdink.subscription.entity.TrainerMembershipStatus;
import com.app.bdink.subscription.entity.TrainerSubscription;
import com.app.bdink.subscription.repository.SubscriptionPlanRepository;
import com.app.bdink.subscription.repository.TrainerSubscriptionRepository;
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
public class TrainerSubscriptionService {

    private final TrainerSubscriptionRepository trainerSubscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final TrainerService trainerService;
    private final TrainerQrService trainerQrService;

    @Transactional(readOnly = true)
    public TrainerSubscription findById(Long id) {
        return trainerSubscriptionRepository.findById(id)
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
    public TrainerSubscription getActiveSubscription(Long trainerId) {
        Trainer trainer = trainerService.getActiveTrainer(trainerId);
        return trainerSubscriptionRepository.findByTrainerAndSubscriptionStatus(trainer, TrainerMembershipStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND, Error.NOT_FOUND.getMessage()));
    }

    @Transactional
    public TrainerSubscription createSubscription(Long trainerId, Long subscriptionPlanId,
                                                  LocalDate paymentDate, boolean autoRenew) {
        Trainer trainer = trainerService.getActiveTrainer(trainerId);
        SubscriptionPlan subscriptionPlan = findPlanById(subscriptionPlanId);

        trainerSubscriptionRepository.findByTrainerAndSubscriptionStatus(trainer, TrainerMembershipStatus.ACTIVE)
                .ifPresent(subscription -> {
                    throw new CustomException(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage());
                });

        TrainerSubscription subscription = TrainerSubscription.create(trainer, subscriptionPlan, paymentDate, autoRenew);
        return trainerSubscriptionRepository.save(subscription);
    }

    @Transactional
    public TrainerSubscription createSubscriptionForMember(Member member, Long subscriptionPlanId,
                                                           LocalDate paymentDate, boolean autoRenew) {
        Trainer trainer = trainerService.getOrCreatePaidTrainer(member);
        trainerQrService.ensureTrainerQr(trainer);
        SubscriptionPlan subscriptionPlan = findPlanById(subscriptionPlanId);

        trainerSubscriptionRepository.findByTrainerAndSubscriptionStatus(trainer, TrainerMembershipStatus.ACTIVE)
                .ifPresent(subscription -> {
                    throw new CustomException(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage());
                });

        TrainerSubscription subscription = TrainerSubscription.create(trainer, subscriptionPlan, paymentDate, autoRenew);
        return trainerSubscriptionRepository.save(subscription);
    }

    @Transactional
    public TrainerSubscription renewSubscription(Long subscriptionId, LocalDate paymentDate) {
        TrainerSubscription subscription = findById(subscriptionId);
        subscription.renew(paymentDate);
        return subscription;
    }

    @Transactional
    public TrainerSubscription cancelSubscription(Long subscriptionId, LocalDate canceledDate) {
        TrainerSubscription subscription = findById(subscriptionId);
        subscription.cancel(canceledDate);
        return subscription;
    }

    @Transactional
    public TrainerSubscription expireSubscription(Long subscriptionId, LocalDate expiredDate) {
        TrainerSubscription subscription = findById(subscriptionId);
        subscription.expire(expiredDate);
        return subscription;
    }

    @Transactional
    public TrainerSubscription markPaymentFailed(Long subscriptionId) {
        TrainerSubscription subscription = findById(subscriptionId);
        subscription.markPaymentFailed();
        return subscription;
    }

    @Transactional(readOnly = true)
    public List<TrainerSubscription> getBillingDueSubscriptions(LocalDate date) {
        return trainerSubscriptionRepository.findAllBySubscriptionStatusAndAutoRenewTrueAndNextBillingDateLessThanEqual(
                TrainerMembershipStatus.ACTIVE, date
        );
    }

    @Transactional(readOnly = true)
    public List<TrainerSubscription> getExpiredSubscriptions(LocalDate date) {
        return trainerSubscriptionRepository.findAllByExpiredDateBeforeAndSubscriptionStatus(
                date, TrainerMembershipStatus.ACTIVE
        );
    }

    @Transactional
    public void processRecurringBilling(LocalDate today) {
        List<TrainerSubscription> dueSubscriptions = getBillingDueSubscriptions(today);

        // 실제 정기결제 연동 전까지는 청구 대상만 로그로 남긴다.
        for (TrainerSubscription subscription : dueSubscriptions) {
            log.info("Recurring billing due. subscriptionId={}, trainerId={}, nextBillingDate={}",
                    subscription.getId(), subscription.getTrainer().getId(), subscription.getNextBillingDate());
        }
    }

    @Transactional
    public void expireDueSubscriptions(LocalDate today) {
        List<TrainerSubscription> expiredSubscriptions = getExpiredSubscriptions(today);

        for (TrainerSubscription subscription : expiredSubscriptions) {
            subscription.expire(subscription.getExpiredDate());
        }

        if (!expiredSubscriptions.isEmpty()) {
            log.info("Expired trainer subscriptions updated: {}", expiredSubscriptions.size());
        }
    }
}
