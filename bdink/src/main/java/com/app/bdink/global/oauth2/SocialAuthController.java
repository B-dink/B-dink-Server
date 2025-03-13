package com.app.bdink.global.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth2")
public class SocialAuthController {
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<?> signIn(
            @RequestParam("code") String socialAccessToken,
            @RequestParam("provider") String provider
    ) {
        return ResponseEntity.ok(authService.signIn(provider, socialAccessToken));
    }


}

