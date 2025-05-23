package com.app.bdink.version.controller;

import com.app.bdink.version.service.VersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VersionController {

    private final VersionService versionService;


}
