package wooteco.subway.auth.application;

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
        Member member = memberDao.findByEmail(email);
        if (member == null) {
            throw new UnauthorizedException();
        }

        member.validatePassword(password);

        String jws = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new TokenResponse(jws);
    }

    public Long getPayload(String token) {
        if (!validateToken(token)) {
            throw new UnauthorizedException();
        }
        return Long.parseLong(jwtTokenProvider.getPayload(token));
    }

    private boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }
}
