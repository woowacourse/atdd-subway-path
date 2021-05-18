package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.web.member.request.TokenRequest;
import wooteco.subway.web.member.response.TokenResponse;
import wooteco.subway.service.member.AuthService;

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
