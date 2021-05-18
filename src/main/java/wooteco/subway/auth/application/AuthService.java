package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.domain.AuthorizationPayLoad;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(final TokenRequest tokenRequest) {
        return jwtTokenProvider.createToken(tokenRequest.getEmail());
    }

    public AuthorizationPayLoad getPayLoad(final String token) {
        jwtTokenProvider.validateToken(token);
        final String payload = jwtTokenProvider.getPayload(token);
        return new AuthorizationPayLoad(payload);
    }
}
