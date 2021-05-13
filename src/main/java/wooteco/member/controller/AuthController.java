package wooteco.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.member.controller.dto.request.LoginRequestDto;
import wooteco.member.controller.dto.response.LoginTokenResponseDto;
import wooteco.member.service.AuthService;

@RequestMapping("/api")
@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginTokenResponseDto> loginWithToken(@RequestBody LoginRequestDto loginRequestDto) {
        LoginTokenResponseDto loginTokenResponseDto = authService.createToken(loginRequestDto);
        return ResponseEntity.ok(loginTokenResponseDto);
    }
}
