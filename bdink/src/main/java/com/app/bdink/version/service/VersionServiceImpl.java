package com.app.bdink.version.service;

import com.app.bdink.version.respository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VersionServiceImpl {

    private final VersionRepository versionRepository;


}
