package com.app.bdink.version.controller;

import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.version.controller.dto.VersionRequest;
import com.app.bdink.version.service.VersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VersionController {

    private final VersionService versionService;

    @PostMapping
    public RspTemplate<?> postVersion(@RequestBody VersionRequest request) {

    }
}
