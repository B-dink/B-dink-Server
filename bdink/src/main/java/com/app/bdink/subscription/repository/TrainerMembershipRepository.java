package com.app.bdink.subscription.repository;

import com.app.bdink.subscription.entity.TrainerMembershipStatus;
import com.app.bdink.subscription.entity.TrainerMembership;
import com.app.bdink.trainer.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainerMembershipRepository extends JpaRepository<TrainerMembership, Long> {

    Optional<TrainerMembership> findByTrainerAndTrainerMembershipStatus(Trainer trainer, TrainerMembershipStatus trainerMembershipStatus);

    List<TrainerMembership> findAllByTrainerMembershipStatusAndAutoRenewTrueAndNextBillingDateLessThanEqual(
            TrainerMembershipStatus trainerMembershipStatus, LocalDate date
    );

    List<TrainerMembership> findAllByExpiredDateBeforeAndTrainerMembershipStatus(LocalDate date, TrainerMembershipStatus trainerMembershipStatus);
}
