package wooteco.subway.auth.ui;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.UnauthorizedException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.dto.MemberResponse;

@RestController
public class AuthController {

    private static final int THIRTY_MINUTE = 60 * 30;

    private final JwtTokenProvider tokenProvider;
    private final MemberService memberService;

    public AuthController(JwtTokenProvider tokenProvider, MemberService memberService) {
        this.tokenProvider = tokenProvider;
        this.memberService = memberService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest,
            HttpServletResponse response) {
        if (!memberService.isExist(tokenRequest.getEmail())) {
            throw new UnauthorizedException(
                    String.format("해당 이메일로 가입한 유저가 없습니다. 이메일 : %s", tokenRequest.getEmail()));
        }
        MemberResponse memberByEmail = memberService.findMemberByEmail(tokenRequest.getEmail());
        String accessToken = tokenProvider.createToken(String.valueOf(memberByEmail.getId()));

        setAccessTokenToCookie(response, accessToken);

        return ResponseEntity.ok(new TokenResponse(accessToken));
    }

    private void setAccessTokenToCookie(HttpServletResponse response, String accessToken) {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(THIRTY_MINUTE);
        response.addCookie(accessTokenCookie);
    }
}
