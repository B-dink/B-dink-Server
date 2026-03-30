package com.app.bdink.trainermembership.repository;

import com.app.bdink.trainermembership.entity.TrainerMembershipPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainerMembershipPlanRepository extends JpaRepository<TrainerMembershipPlan, Long> {

    List<TrainerMembershipPlan> findAllByActiveTrue();
}
