package wooteco.subway.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.application.dto.LoginRequestDto;
import wooteco.subway.auth.ui.dto.LoginRequest;
import wooteco.subway.auth.ui.dto.LoginResponse;
import wooteco.subway.auth.ui.dto.TokenRequest;

import javax.validation.Valid;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authService.createToken(
                new LoginRequestDto(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        return ResponseEntity.ok().body(new LoginResponse(token));
    }

    @PostMapping("/auth/token")
    public ResponseEntity<Void> validateToken(@Valid @RequestBody TokenRequest tokenRequest) {
        authService.validateToken(tokenRequest.getToken());

        return ResponseEntity.ok().build();
    }

}
