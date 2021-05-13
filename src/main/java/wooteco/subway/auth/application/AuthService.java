package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.InvalidPasswordException;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {
    private JwtTokenProvider jwtTokenProvider;
    private MemberService memberService;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = memberService.findMemberByEmail(tokenRequest.getEmail());
        if (member.invalidPassword(tokenRequest.getPassword())) {
            throw new InvalidPasswordException();
        }

        String accessToken = jwtTokenProvider.createToken(tokenRequest.getEmail());
        return new TokenResponse(accessToken);
    }

    public Member findMemberByToken(final String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException();
        }

        String email = jwtTokenProvider.getPayload(token);
        return memberService.findMemberByEmail(email);
    }
}
