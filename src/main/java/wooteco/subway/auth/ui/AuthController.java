package wooteco.subway.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.AuthException;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> tokenLogin(@RequestBody TokenRequest tokenRequest) {
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> handle(AuthException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
    }
}
