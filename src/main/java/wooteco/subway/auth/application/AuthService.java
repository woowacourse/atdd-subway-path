package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

import java.util.Optional;

@Service
public class AuthService {

    private JwtTokenProvider jwtTokenProvider;
    private MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        validateLogin(tokenRequest);
        final String accessToken = jwtTokenProvider.createToken(tokenRequest.getEmail());
        return new TokenResponse(accessToken);
    }

    private void validateLogin(TokenRequest tokenRequest) {
        final String email = tokenRequest.getEmail();
        final String password = tokenRequest.getPassword();
        final Optional<Member> member = memberDao.findByEmail(email);
        if (!(member.isPresent() && member.get().isSamePassword(password))) {
            throw new AuthorizationException();
        }
    }
}
