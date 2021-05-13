package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {

    private JwtTokenProvider jwtTokenProvider;
    private MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        if (!validateLogin(tokenRequest)) {
            throw new AuthorizationException();
        }

        final String accessToken = jwtTokenProvider.createToken(tokenRequest.getEmail());
        return new TokenResponse(accessToken);
    }

    private boolean validateLogin(TokenRequest tokenRequest) {
        final String email = tokenRequest.getEmail();
        final String password = tokenRequest.getPassword();
        final Member member = memberDao.findByEmail(email);
        return member.getPassword().equals(password);
    }
}
