package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthService(JwtTokenProvider jwtTokenProvider,
        MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public TokenResponse generateToken(TokenRequest tokenRequest) {
        MemberResponse memberResponse = memberService.findMemberByTokenRequest(tokenRequest);
        return new TokenResponse(jwtTokenProvider.createToken(memberResponse.getEmail()));
    }

    public String getPayLoad(String token) {
        return jwtTokenProvider.getPayload(token);
    }
}
