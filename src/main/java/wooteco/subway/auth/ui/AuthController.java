package wooteco.subway.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.AuthException;

@RestController
@RequestMapping("/api")
public class AuthController {
    // TODO: 로그인(토큰 발급) 요청 처리하기
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> tokenLogin(@RequestBody TokenRequest tokenRequest) {
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> handle(AuthException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
    }
}
