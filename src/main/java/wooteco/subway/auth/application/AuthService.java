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

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public TokenResponse login(final TokenRequest request) {
        memberService.validateMember(request);
        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public MemberResponse findByToken(String token) {
        String email = jwtTokenProvider.getPayload(token);
        return memberService.findMemberByEmail(email);
    }
}
