package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthService(JwtTokenProvider jwtTokenProvider,
        MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public TokenResponse login(String email, String password) {
        Member member = memberService.findMember(email);
        if (!password.equals(member.getPassword())) {
            throw new IllegalArgumentException("로그인 실패");
        }
        String jws = jwtTokenProvider.createToken(email);
        return new TokenResponse(jws);
    }
}
