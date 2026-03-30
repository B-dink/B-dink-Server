package com.app.bdink.subscription.repository;

import com.app.bdink.subscription.entity.SubscriptionStatus;
import com.app.bdink.subscription.entity.TrainerSubscription;
import com.app.bdink.trainer.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainerSubscriptionRepository extends JpaRepository<TrainerSubscription, Long> {

    Optional<TrainerSubscription> findByTrainerAndSubscriptionStatus(Trainer trainer, SubscriptionStatus subscriptionStatus);

    List<TrainerSubscription> findAllBySubscriptionStatusAndAutoRenewTrueAndNextBillingDateLessThanEqual(
            SubscriptionStatus subscriptionStatus, LocalDate date
    );

    List<TrainerSubscription> findAllByExpiredDateBeforeAndSubscriptionStatus(LocalDate date, SubscriptionStatus subscriptionStatus);
}
