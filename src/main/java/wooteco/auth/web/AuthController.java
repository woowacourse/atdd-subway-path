package wooteco.auth.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.service.AuthService;
import wooteco.auth.domain.Token;
import wooteco.auth.web.dto.TokenRequest;
import wooteco.auth.web.dto.TokenResponse;

@RequestMapping("/api/login/token")
@RestController
public class AuthController {
    // TODO: 로그인(토큰 발급) 요청 처리하기

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity login(@RequestBody TokenRequest tokenRequest) {
        Token token = authService.login(tokenRequest.getEmail(), tokenRequest.getPassword());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
