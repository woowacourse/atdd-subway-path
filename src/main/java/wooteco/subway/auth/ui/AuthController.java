package wooteco.subway.auth.ui;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<Void> tokenLogin(@RequestBody TokenRequest tokenRequest) {
        final TokenResponse tokenResponse = authService.createToken(tokenRequest);
        ResponseCookie responseCookie =
                ResponseCookie.from("accessToken", tokenResponse.getAccessToken()).path("/").maxAge(1800L).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return ResponseEntity.ok().headers(headers).build();
    }
}
