package com.app.bdink.version.service;

import com.app.bdink.version.controller.dto.ForceUpdateInfo;
import com.app.bdink.version.entity.Platform;
import com.app.bdink.version.entity.Version;

public interface VersionService {

    Long addVersion(Version version);
    Version getVersion(Long id);
    void deleteVersion(Long id);

    Boolean isUpdateRequired(String currentVersion, Platform platform);
    ForceUpdateInfo checkForceUpdateInfo(String currentVersion, Platform platform);
    Version getLatestVersion(Platform platform);
}
