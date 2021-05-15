package wooteco.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.member.controller.dto.request.SignInRequestDto;
import wooteco.member.controller.dto.response.SignInResponseDto;
import wooteco.member.service.AuthService;

@RequestMapping("/api")
@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDto> loginWithToken(@RequestBody SignInRequestDto signInRequestDto) {
        SignInResponseDto tokenResponse = authService.createToken(signInRequestDto);
        return ResponseEntity.ok(tokenResponse);
    }
}
