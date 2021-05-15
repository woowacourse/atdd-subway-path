package wooteco.subway.auth.ui;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/login")
public class AuthController {
    // TODO: 로그인(토큰 발급) 요청 처리하기
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ResponseEntity<Void> login(@RequestBody TokenRequest tokenRequest, HttpServletResponse res) {
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        ResponseCookie cookie = authService.createCookie(tokenResponse.getAccessToken());
        res.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok().build();
    }
}
