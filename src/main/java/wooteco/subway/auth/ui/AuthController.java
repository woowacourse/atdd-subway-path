package wooteco.subway.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;

@RestController
@RequestMapping("/login")
public class AuthController {
    // TODO: 로그인(토큰 발급) 요청 처리하기
    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> getTokenResponseByUserInfo (@RequestBody TokenRequest tokenRequest) {
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }
}
