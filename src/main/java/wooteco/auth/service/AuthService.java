package wooteco.subway.auth.service;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.UnauthorizedException;
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

    public TokenResponse login(String email, String password) {
        Member member = memberDao.findByEmail(email)
            .orElseThrow(UnauthorizedException::new);

        member.validatePassword(password);

        String jwt = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new TokenResponse(jwt);
    }

    public void validateToken(String token) {
        if (jwtTokenProvider.validateToken(token)) {
            return;
        }
        throw new UnauthorizedException();
    }

    public Long getPayload(String token) {
        return Long.parseLong(jwtTokenProvider.getPayload(token));
    }
}
