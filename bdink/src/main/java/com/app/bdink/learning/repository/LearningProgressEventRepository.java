package com.app.bdink.learning.repository;

import com.app.bdink.learning.entity.LearningProgressEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningProgressEventRepository extends JpaRepository<LearningProgressEvent, Long> {
}
