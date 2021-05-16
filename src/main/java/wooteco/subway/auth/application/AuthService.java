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
        Member member = memberDao.findByEmail(email)
            .orElseThrow(UnauthorizedException::new);

        validatePassword(password, member.getPassword());

        String jwt = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new TokenResponse(jwt);
    }

    private void validatePassword(String password, String savedPassword) {
        if (password.equals(savedPassword)) {
            return;
        }
        throw new IllegalArgumentException("로그인 실패");
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
