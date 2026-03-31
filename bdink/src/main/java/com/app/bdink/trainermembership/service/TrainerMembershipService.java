package com.app.bdink.trainermembership.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import com.app.bdink.trainermembership.entity.TrainerMembership;
import com.app.bdink.trainermembership.entity.TrainerMembershipPlan;
import com.app.bdink.trainermembership.entity.TrainerMembershipStatus;
import com.app.bdink.trainermembership.repository.TrainerMembershipRepository;
import com.app.bdink.trainermembership.repository.TrainerMembershipPlanRepository;
import com.app.bdink.trainer.entity.Trainer;
import com.app.bdink.trainer.service.TrainerQrService;
import com.app.bdink.trainer.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerMembershipService {

    private final TrainerMembershipRepository trainerMembershipRepository;
    private final TrainerMembershipPlanRepository trainerMembershipPlanRepository;
    private final TrainerService trainerService;
    private final TrainerQrService trainerQrService;

    @Transactional(readOnly = true)
    public TrainerMembership findById(Long id) {
        return trainerMembershipRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND, Error.NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    public TrainerMembershipPlan findPlanById(Long planId) {
        return trainerMembershipPlanRepository.findById(planId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND, Error.NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    public List<TrainerMembershipPlan> getActivePlans() {
        return trainerMembershipPlanRepository.findAllByActiveTrue();
    }

    @Transactional(readOnly = true)
    public TrainerMembership getActiveMembership(Long trainerId) {
        Trainer trainer = trainerService.getActiveTrainer(trainerId);
        return trainerMembershipRepository.findByTrainerAndTrainerMembershipStatus(trainer, TrainerMembershipStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND, Error.NOT_FOUND.getMessage()));
    }

    @Transactional
    public TrainerMembership createMembership(Long trainerId, Long trainerMembershipPlanId,
                                              LocalDate paymentDate) {
        Trainer trainer = trainerService.getActiveTrainer(trainerId);
        TrainerMembershipPlan trainerMembershipPlan = findPlanById(trainerMembershipPlanId);

        trainerMembershipRepository.findByTrainerAndTrainerMembershipStatus(trainer, TrainerMembershipStatus.ACTIVE)
                .ifPresent(subscription -> {
                    throw new CustomException(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage());
                });

        TrainerMembership membership = TrainerMembership.create(trainer, trainerMembershipPlan, paymentDate);
        return trainerMembershipRepository.save(membership);
    }

    @Transactional
    public TrainerMembership createMembershipForMember(Member member, Long trainerMembershipPlanId,
                                                       LocalDate paymentDate) {
        Trainer trainer = trainerService.getOrCreatePaidTrainer(member);
        trainerQrService.ensureTrainerQr(trainer);
        TrainerMembershipPlan trainerMembershipPlan = findPlanById(trainerMembershipPlanId);

        trainerMembershipRepository.findByTrainerAndTrainerMembershipStatus(trainer, TrainerMembershipStatus.ACTIVE)
                .ifPresent(subscription -> {
                    throw new CustomException(Error.BAD_REQUEST_VALIDATION, Error.BAD_REQUEST_VALIDATION.getMessage());
                });

        TrainerMembership membership = TrainerMembership.create(trainer, trainerMembershipPlan, paymentDate);
        return trainerMembershipRepository.save(membership);
    }

    @Transactional
    public TrainerMembership expireMembership(Long membershipId, LocalDate expiredDate) {
        TrainerMembership membership = findById(membershipId);
        membership.expire(expiredDate);
        return membership;
    }

    @Transactional(readOnly = true)
    public List<TrainerMembership> getExpiredMemberships(LocalDate date) {
        return trainerMembershipRepository.findAllByExpiredDateBeforeAndTrainerMembershipStatus(
                date, TrainerMembershipStatus.ACTIVE
        );
    }

    @Transactional
    public void expireDueMemberships(LocalDate today) {
        List<TrainerMembership> expiredMemberships = getExpiredMemberships(today);

        for (TrainerMembership membership : expiredMemberships) {
            membership.expire(membership.getExpiredDate());
        }
    }
}
