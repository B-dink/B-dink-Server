package com.app.bdink.version.service;

import com.app.bdink.version.entity.Platform;
import com.app.bdink.version.entity.Version;
import com.app.bdink.version.respository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class VersionServiceImpl implements VersionService {

    private final VersionRepository versionRepository;

    @Override
    public Long addVersion(Version version) {
        return versionRepository.save(version).getId();
    }

    @Override
    public Version getVersion(Long id) {
        return versionRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Version not found")
        );
    }

    @Override
    public void updateVersion(Version version) {
    }

    @Override
    public void deleteVersion(Long id) {
        if (!versionRepository.existsById(id)) {
            throw new NoSuchElementException("Version not found");
        }
        versionRepository.deleteById(id);
    }

    @Override
    public Boolean isUpdateRequired(String currentVersion, Platform platform) {
        Version latestVersion = getLatestVersion(platform);
        return !currentVersion.equals(latestVersion.getCurrentVersion());
    }

    @Override
    public Boolean isForceUpdateRequired(String currentVersion, Platform platform) {
        Version latestVersion = getLatestVersion(platform);
        if (latestVersion.getForceUpdateRequired()) {
            return compareVersions(currentVersion, latestVersion.getMinimumRequiredVersion()) < 0;
        }
        return false;
    }

    @Override
    public Version getLatestVersion(Platform platform) {
        List<Version> versionsByPlatform = versionRepository.findAllByPlatform(platform);

        return versionsByPlatform.stream()
                .max((v1, v2) -> compareVersions(v1.getCurrentVersion(), v2.getCurrentVersion()))
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
