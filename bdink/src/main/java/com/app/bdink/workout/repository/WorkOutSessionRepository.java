package com.app.bdink.workout.repository;

import com.app.bdink.workout.entity.WorkOutSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkOutSessionRepository extends JpaRepository<WorkOutSession, Long> {
}
