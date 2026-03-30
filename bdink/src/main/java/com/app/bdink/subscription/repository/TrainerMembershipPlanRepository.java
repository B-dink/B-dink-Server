package com.app.bdink.subscription.repository;

import com.app.bdink.subscription.entity.TrainerMembershipPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainerMembershipPlanRepository extends JpaRepository<TrainerMembershipPlan, Long> {

    List<TrainerMembershipPlan> findAllByActiveTrue();
}
