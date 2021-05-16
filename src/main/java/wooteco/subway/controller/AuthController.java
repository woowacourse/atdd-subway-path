package wooteco.subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.dto.TokenRequest;
import wooteco.subway.dto.TokenResponse;
import wooteco.subway.service.AuthService;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest) {
        String accessToken = authService.issueToken(tokenRequest.getEmail(), tokenRequest.getPassword());
        return ResponseEntity.ok(new TokenResponse(accessToken));
    }

}
