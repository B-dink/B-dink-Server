package com.app.bdink.external.kollus.repository;

import com.app.bdink.external.kollus.entity.KollusMedia;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface KollusMediaRepository extends JpaRepository<KollusMedia, Long> {
    Optional<KollusMedia> findByLectureId (Long lectureId);
    Optional<KollusMedia> findByMediaContentKey(String mediaContentKey);
}

