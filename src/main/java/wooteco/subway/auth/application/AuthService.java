package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.JwtNotAuthorizationException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
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

    public TokenResponse login(final TokenRequest request) {
        Member member = memberService.findMemberByEmail(request);
        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findByToken(String token) {
        if(!jwtTokenProvider.validateToken(token)) {
            throw new JwtNotAuthorizationException();
        }
        String id = jwtTokenProvider.getId(token);
        String email = jwtTokenProvider.getEmail(token);
        return new LoginMember(id, email);
    }
}
