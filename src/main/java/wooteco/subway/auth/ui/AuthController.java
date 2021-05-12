package wooteco.subway.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.member.application.MemberService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {
    // 로그인(토큰 발급) 요청 처리하기
    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@Valid @RequestBody TokenRequest tokenRequest) {
        TokenResponse tokenResponse = memberService.createToken(tokenRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }
}
