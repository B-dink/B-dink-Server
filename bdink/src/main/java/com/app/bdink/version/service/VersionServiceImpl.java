package com.app.bdink.version.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.version.controller.dto.ForceUpdateInfo;
import com.app.bdink.version.entity.Platform;
import com.app.bdink.version.entity.Version;
import com.app.bdink.version.respository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class VersionServiceImpl implements VersionService {

    private final VersionRepository versionRepository;

    @Override
    public Long addVersion(Version version) {
        if (versionRepository.existsByVersionAndPlatform(version.getVersion(), version.getPlatform())) {
            throw new CustomException(Error.VERSION_ALREADY_EXISTS, Error.VERSION_ALREADY_EXISTS.getMessage());
        }

        Version latestVersion = getLatestVersion(version.getPlatform());
        if (latestVersion != null &&
                compareVersions(version.getVersion(), latestVersion.getVersion()) <= 0) {
            throw new CustomException(Error.INVALID_VERSION_ORDER, Error.INVALID_VERSION_ORDER.getMessage());
        }
        return versionRepository.save(version).getId();
    }

    @Override
    public Version getVersion(Long id) {
        return versionRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Version not found")
        );
    }

    @Override
    public Boolean isUpdateRequired(String currentVersion, Platform platform) {
        Version latestVersion = getLatestVersion(platform);
        return !currentVersion.equals(latestVersion.getVersion());
    }

    @Override
    public ForceUpdateInfo checkForceUpdateInfo(String currentVersion, Platform platform) {
        List<Version> allVersions = versionRepository.findAllByPlatform(platform);

        // 현재 버전보다 높으면서 강제 업데이트가 필요한 버전들 중 가장 높은 버전 찾기
        Optional<Version> forceUpdateVersion = allVersions.stream()
                .filter(v -> Boolean.TRUE.equals(v.getForceUpdateRequired()))
                .filter(v -> compareVersions(currentVersion, v.getVersion()) < 0)
                .max((v1, v2) -> compareVersions(v1.getVersion(), v2.getVersion()));

        return forceUpdateVersion.map(
                version -> new ForceUpdateInfo(true, version.getVersion()))
                .orElseGet(
                        () -> new ForceUpdateInfo(false, null));
    }

    @Override
    public Version getLatestVersion(Platform platform) {
        List<Version> versionsByPlatform = versionRepository.findAllByPlatform(platform);

        return versionsByPlatform.stream()
                .max((v1, v2) -> compareVersions(v1.getVersion(), v2.getVersion()))
                .orElseThrow(() -> new NoSuchElementException("해당 플랫폼의 버전을 찾을 수 없습니다: " + platform));
    }

    /**
     * 버전 비교 유틸리티 메소드
     * @param version1 비교할 버전1
     * @param version2 비교할 버전2
     * @return version1이 낮으면 음수, 같으면 0, 높으면 양수
     */
    private int compareVersions(String version1, String version2) {
        String[] v1Parts = version1.split("\\.");
        String[] v2Parts = version2.split("\\.");

        int maxLength = Math.max(v1Parts.length, v2Parts.length);

        for (int i = 0; i < maxLength; i++) {
            int v1Part = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
            int v2Part = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;

            if (v1Part != v2Part) {
                return Integer.compare(v1Part, v2Part);
            }
        }
        return 0;
    }
}
