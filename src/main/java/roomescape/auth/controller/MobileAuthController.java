package roomescape.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.auth.repository.TokenStorage;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.Member;

@RestController
@RequestMapping("/api/mobile")
public class MobileAuthController {

    private final AuthService authService;
    private final TokenStorage tokenStorage;

    public MobileAuthController(AuthService authService, TokenStorage tokenStorage) {
        this.authService = authService;
        this.tokenStorage = tokenStorage;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        Member member = authService.login(request);

        String token = tokenStorage.createToken(member.getId());

        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            tokenStorage.removeToken(token);
        }

        return ResponseEntity.ok().build();
    }
}
