package wooteco.subway.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.member.controller.request.TokenRequest;
import wooteco.subway.member.controller.response.TokenResponse;
import wooteco.subway.member.service.AuthService;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest) {
        final TokenResponse tokenResponse = authService.createToken(tokenRequest);
        return ResponseEntity.ok(tokenResponse);
    }

}
