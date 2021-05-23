package wooteco.subway.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.application.AuthService;
import wooteco.subway.presentation.dto.request.TokenRequest;
import wooteco.subway.presentation.dto.response.TokenResponse;

import javax.validation.Valid;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid TokenRequest tokenRequest) {
        TokenResponse accessToken = authService.login(tokenRequest);
        return ResponseEntity.ok(accessToken);
    }
}
