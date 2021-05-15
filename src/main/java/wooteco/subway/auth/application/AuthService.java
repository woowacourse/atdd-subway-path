package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;

import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
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
        final Member member =  memberService.findMember(tokenRequest.getEmail());
        authorize(member, tokenRequest);
        String accessToken = jwtTokenProvider.createToken(member.getId());
        return new TokenResponse(accessToken);
    }

    public void authorize(Member member, TokenRequest tokenRequest) {
        final Member requestMember = tokenRequest.toEntity();
        member.authorize(requestMember);
    }
}
