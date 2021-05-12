package wooteco.subway.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.UnauthorizedException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.application.MemberService;

@RestController
public class AuthController {

    private final JwtTokenProvider tokenProvider;
    private final MemberService memberService;

    public AuthController(JwtTokenProvider tokenProvider, MemberService memberService) {
        this.tokenProvider = tokenProvider;
        this.memberService = memberService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest) {
        if (!memberService.isExist(tokenRequest.getEmail())) {
            throw new UnauthorizedException(
                    String.format("해당 이메일로 된 유저가 없습니다. 이메일 : %s", tokenRequest.getEmail()));
        }
        String accessToken = tokenProvider.createToken(tokenRequest.getEmail());
        return ResponseEntity.ok(new TokenResponse(accessToken));
    }
}
