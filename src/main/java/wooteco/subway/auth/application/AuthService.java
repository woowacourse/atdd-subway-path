package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.exception.UnauthorizedException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class AuthService {

    private final JwtTokenProvider tokenProvider;
    private final MemberService memberService;

    public AuthService(JwtTokenProvider tokenProvider, MemberService memberService) {
        this.tokenProvider = tokenProvider;
        this.memberService = memberService;
    }

    public LoginMember getLoginMember(String accessToken) {
        if (!tokenProvider.validateToken(accessToken)) {
            throw new UnauthorizedException("유효하지 않은 토큰이얌");
        }
        String email = tokenProvider.getPayload(accessToken);
        MemberResponse member = memberService.findMemberByEmail(email);
        return member.toLoginMember();
    }
}
