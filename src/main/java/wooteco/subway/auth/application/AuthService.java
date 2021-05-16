package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.application.exception.AuthorizationException;
import wooteco.subway.auth.application.exception.MemberNotFoundException;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = memberDao.findByEmail(tokenRequest.getEmail())
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다!"));

        member.validatePassword(tokenRequest.getPassword());

        String accessToken = jwtTokenProvider.createToken(Long.toString(member.getId()));
        return new TokenResponse(accessToken);
    }

    public String getPayload(String accessToken) {
        validateToken(accessToken);
        return jwtTokenProvider.getPayload(accessToken);
    }

    private void validateToken(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthorizationException("유효하지 않은 토큰입니다!");
        }
    }
}
