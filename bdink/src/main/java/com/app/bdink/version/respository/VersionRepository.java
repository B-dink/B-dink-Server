package com.app.bdink.version.respository;

import com.app.bdink.version.entity.Platform;
import com.app.bdink.version.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {
    Boolean existsByVersionAndPlatform(String version, Platform platform);
    List<Version> findAllByPlatform(Platform platform);
}
