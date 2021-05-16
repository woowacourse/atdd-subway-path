package wooteco.subway.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.dto.MemberResponse;

@Controller
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    public AuthController(AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest tokenRequest) {
        String email = tokenRequest.getEmail();
        MemberResponse memberResponse = memberService.findMemberByEmail(email);
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        return ResponseEntity.ok()
            .body(tokenResponse);
    }
}
