package wooteco.auth.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.auth.dto.TokenRequest;
import wooteco.auth.dto.TokenResponse;
import wooteco.auth.service.AuthService;

import javax.validation.Valid;

import static wooteco.util.ValidationUtil.validateRequestedParameter;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid TokenRequest tokenRequest, BindingResult bindingResult) {
        validateRequestedParameter(bindingResult);

        String accessToken = authService.issueToken(tokenRequest.getEmail(), tokenRequest.getPassword());
        return ResponseEntity.ok(new TokenResponse(accessToken));
    }

}
