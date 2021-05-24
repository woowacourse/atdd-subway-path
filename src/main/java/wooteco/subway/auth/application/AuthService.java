package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.domain.AuthorizationPayLoad;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.application.MemberService;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Transactional(readOnly = true)
    public String createToken(final TokenRequest tokenRequest) {
        if (!memberService.isExistUser(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            throw new AuthorizedException("올바른 사용자가 아닙니다.");
        }
        return jwtTokenProvider.createToken(tokenRequest.getEmail());
    }

    public AuthorizationPayLoad getPayLoad(final String token) {
        jwtTokenProvider.validateToken(token);
        final String payload = jwtTokenProvider.getPayload(token);
        return new AuthorizationPayLoad(payload);
    }
}
