package com.app.bdink.subscription.repository;

import com.app.bdink.subscription.entity.TrainerMembershipStatus;
import com.app.bdink.subscription.entity.TrainerSubscription;
import com.app.bdink.trainer.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainerMembershipRepository extends JpaRepository<TrainerSubscription, Long> {

    Optional<TrainerSubscription> findByTrainerAndSubscriptionStatus(Trainer trainer, TrainerMembershipStatus subscriptionStatus);

    List<TrainerSubscription> findAllBySubscriptionStatusAndAutoRenewTrueAndNextBillingDateLessThanEqual(
            TrainerMembershipStatus subscriptionStatus, LocalDate date
    );

    List<TrainerSubscription> findAllByExpiredDateBeforeAndSubscriptionStatus(LocalDate date, TrainerMembershipStatus subscriptionStatus);
}
