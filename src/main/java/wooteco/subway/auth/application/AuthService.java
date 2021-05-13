package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;

@Service
public class AuthService {
    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final MemberDao memberDao, final JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(final TokenRequest tokenRequest) {
        if (checkInvalidLogin(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            throw new AuthorizationException("등록되지 않은 사용자입니다.");
        }
        return new TokenResponse(jwtTokenProvider.createToken(tokenRequest.getEmail()));
    }

    private boolean checkInvalidLogin(final String email, final String password) {
        return !memberDao.isExistMemberByEmailAndPassword(email, password);
    }
}
