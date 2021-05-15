package wooteco.subway.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.dto.TokenRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {

    private static final int THIRTY_MINUTE = 60 * 30;

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<Void> login(@RequestBody TokenRequest tokenRequest,
            HttpServletResponse response) {

        String accessToken = authService.createAccessToken(tokenRequest.getEmail());
        setAccessTokenToCookie(response, accessToken);
        return ResponseEntity.ok().build();
    }

    private void setAccessTokenToCookie(HttpServletResponse response, String accessToken) {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(THIRTY_MINUTE);
        response.addCookie(accessTokenCookie);
    }
}
