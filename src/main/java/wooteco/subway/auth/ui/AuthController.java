package wooteco.subway.auth.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.dto.LoginRequest;
import wooteco.subway.auth.dto.LoginResponse;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.exception.InvalidMemberException;
import wooteco.subway.auth.exception.InvalidTokenException;

@RestController
public class AuthController {
    // TODO: 로그인(토큰 발급) 요청 처리하기

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(
                new LoginResponse(authService.createToken(loginRequest))
        );
    }

    @PostMapping("/auth/token")
    public ResponseEntity<Void> validateToken(@RequestBody TokenRequest tokenRequest) {
        authService.validateToken(tokenRequest.getToken());

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(InvalidMemberException.class)
    private ResponseEntity<Void> handleInvalidMemberException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Void> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(InvalidTokenException.class)
    private ResponseEntity<Void> handleInvalidTokenException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
