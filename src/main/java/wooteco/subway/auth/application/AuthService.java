package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dao.AuthDao;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.member.domain.Member;

import java.util.Optional;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthDao authDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, AuthDao authDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authDao = authDao;
    }
    public TokenResponse createToken(TokenRequest tokenRequest) {
        validateLogin(tokenRequest);
        String accessToken = jwtTokenProvider.createToken(tokenRequest.getEmail());
        return new TokenResponse(accessToken);
    }

    private void validateLogin(TokenRequest tokenRequest) {
        Optional<Member> foundMember = authDao.findByEmail(tokenRequest.getEmail());

        if (!foundMember.isPresent()) {
            throw new AuthorizationException();
        }

        if (foundMember.get().isNotSamePassword(tokenRequest.getPassword())) {
            throw new AuthorizationException();
        }
    }
}
