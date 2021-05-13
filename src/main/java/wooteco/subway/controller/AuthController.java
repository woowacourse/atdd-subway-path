package wooteco.subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.dto.MemberResponse;
import wooteco.subway.dto.TokenRequest;
import wooteco.subway.dto.TokenResponse;
import wooteco.subway.service.AuthService;
import wooteco.subway.service.MemberService;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // TODO: 로그인(토큰 발급) 요청 처리하기
    // todo: login에 대한 postMapping
    // MemberService

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest) {
        String accessToken = authService.issueToken(tokenRequest.getEmail(), tokenRequest.getPassword());
        return ResponseEntity.ok(new TokenResponse(accessToken));
    }

}
