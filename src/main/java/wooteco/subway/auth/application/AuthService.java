package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.InvalidTokenException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;

import java.util.Optional;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        validateLogin(tokenRequest);
        String accessToken = jwtTokenProvider.createToken(tokenRequest.getEmail());
        return new TokenResponse(accessToken);
    }

    private void validateLogin(TokenRequest tokenRequest) {
        Optional<Member> foundMember = Optional.ofNullable(memberDao.findByEmail(tokenRequest.getEmail())
                .orElseThrow(AuthorizationException::new));

        if (foundMember.get().hasDifferentPassword(tokenRequest.getPassword())) {
            throw new AuthorizationException();
        }
    }

    public LoginMember findMemberByToken(String token) {
        if (jwtTokenProvider.validateToken(token)) {
            String payload = jwtTokenProvider.getPayload(token);
            return findMember(payload);
        }
        throw new InvalidTokenException();
    }

    public LoginMember findMember(String principal) {
        Optional<Member> foundMember = Optional.ofNullable(memberDao.findByEmail(principal)
                .orElseThrow(AuthorizationException::new));

        return LoginMember.of(foundMember.get());
    }
}
