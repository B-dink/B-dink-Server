package com.app.bdink.global.oauth2;

import com.app.bdink.global.oauth2.domain.RefreshToken;
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

    @PostMapping("/token")
    public ResponseEntity<?> reIssueToken(@RequestBody RefreshToken token) {
        return ResponseEntity.ok(authService.reIssueToken(token));
    }


}

