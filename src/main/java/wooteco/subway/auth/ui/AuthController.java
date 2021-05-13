package wooteco.subway.auth.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;

@RestController
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // TODO: 로그인(토큰 발급) 요청 처리하기
    @PostMapping("/login/token")
    public ResponseEntity tokenLogin(@RequestBody TokenRequest tokenRequest) {
        // TODO: TokenRequest 값을 메서드 파라미터로 받아오기 (hint: @RequestBody)
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity authorizationExceptionHandle(AuthorizationException exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}


