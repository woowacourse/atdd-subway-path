package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.application.MemberService;

@Service
public class AuthService {

    private JwtTokenProvider jwtTokenProvider;
    private MemberService memberService;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        if (checkInvalidLogin(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            throw new AuthorizationException();
        }

        String accessToken = jwtTokenProvider.createToken(tokenRequest.getEmail());
        return new TokenResponse(accessToken);
    }

    private boolean checkInvalidLogin(String email, String password) {
        return !(memberService.doesEmailExist(email) && memberService.doesPasswordExist(password));
    }

    public String getPayload(String token) {
        return jwtTokenProvider.getPayload(token);
    }
}
