package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;

import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        final MemberResponse memberResponse = memberService.findMemberByEmail(tokenRequest.getEmail());
        authorize(memberResponse, tokenRequest);
        String accessToken = jwtTokenProvider.createToken(memberResponse.getId());
        return new TokenResponse(accessToken);
    }

    public void authorize(final MemberResponse memberResponse, final TokenRequest tokenRequest) {
        final Member savedMember = memberResponse.toEntity();
        final Member requestMember = tokenRequest.toEntity();
        savedMember.authorize(requestMember);
    }
}
