package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;

import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.application.AuthorizationException;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        final Member member = memberService.findMemberByEmail(tokenRequest.getEmail());
        authorize(member, tokenRequest);
        String accessToken = jwtTokenProvider.createToken(member.getId());
        return new TokenResponse(accessToken);
    }

    public void authorize(final Member member, final TokenRequest tokenRequest) {
        final Member requestMember = tokenRequest.toEntity();
        member.authorize(requestMember);
    }

    public void validateToken(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthorizationException("유효하지 않은 토큰입니다");
        }
    }

    public String getPayload(String accessToken) {
        return jwtTokenProvider.getPayload(accessToken);
    }
}
