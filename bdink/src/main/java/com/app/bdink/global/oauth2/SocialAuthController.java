package com.app.bdink.global.oauth2;

import com.app.bdink.global.oauth2.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


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

    @PostMapping("/sign-out")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> signOut(Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        authService.signOut(userId);
        return ResponseEntity.noContent().build();
    }


}

