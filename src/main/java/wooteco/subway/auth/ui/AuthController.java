package wooteco.subway.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.dto.MemberResponse;

@Controller
public class AuthController {

    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest tokenRequest) {
        String email = tokenRequest.getEmail();
        MemberResponse memberResponse = memberService.findMemberByEmail(email);
        TokenResponse tokenResponse = memberService.createToken(tokenRequest);
        return ResponseEntity.ok()
            .body(tokenResponse);
    }
}
