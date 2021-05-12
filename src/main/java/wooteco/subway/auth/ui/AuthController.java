package wooteco.subway.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;

@RestController
public class AuthController {

    private final JwtTokenProvider tokenProvider;

    public AuthController(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest) {
        String accessToken = tokenProvider.createToken(tokenRequest.getEmail());
        return ResponseEntity.ok(new TokenResponse(accessToken));
    }
}
