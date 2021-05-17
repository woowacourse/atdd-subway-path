package wooteco.subway.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;

import javax.validation.Valid;
import java.util.Objects;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody TokenRequest tokenRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        final TokenResponse tokenResponse = authService.createToken(tokenRequest);
        return ResponseEntity.ok(tokenResponse);
    }
}
