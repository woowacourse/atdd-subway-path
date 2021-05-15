package wooteco.subway.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    // XXX :: Member를 Controller에서 확인하고, email만 넘겨서 token을 생성하는 것과 지금
    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody final TokenRequest tokenRequest) {
        final String token = authService.createToken(tokenRequest);
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
