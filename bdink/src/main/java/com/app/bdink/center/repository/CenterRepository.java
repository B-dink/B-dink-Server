package com.app.bdink.center.repository;

import com.app.bdink.center.controller.dto.CenterStatus;
import com.app.bdink.center.entity.Center;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CenterRepository extends JpaRepository<Center, Long> {
    Optional<Center> findById(Long id);
    List<Center> findByStatus(CenterStatus status);
}
