package com.app.bdink.center.repository;

import com.app.bdink.center.entity.Center;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CenterRepository extends JpaRepository<Center, Long> {
    Optional<Center> findByCenterId(Long centerId);

    Optional<Center> findById(Long id);

    Optional<Center> findByCenterName(String centerName);

    Optional<Center> findByQrCode(String qrCode);
}
