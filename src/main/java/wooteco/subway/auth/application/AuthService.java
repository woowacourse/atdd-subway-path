package wooteco.subway.auth.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.InvalidTokenException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberResponse;

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

    public MemberResponse findMemberByToken(String token) {
        if (jwtTokenProvider.validateToken(token)) {
            String payload = jwtTokenProvider.getPayload(token);
            return findMember(payload);
        }
        throw new InvalidTokenException();
    }

    public MemberResponse findMember(String principal) {
        Optional<Member> foundMember = memberDao.findByEmail(principal);

        if (foundMember.isPresent()) {
            return MemberResponse.of(foundMember.get());
        }
        throw new AuthorizationException();
    }

    private void validateLogin(TokenRequest tokenRequest) {
        Optional<Member> foundMember = memberDao.findByEmail(tokenRequest.getEmail());

        if (!foundMember.isPresent()) {
            throw new AuthorizationException();
        }

        if (foundMember.get().isNotSamePassword(tokenRequest.getPassword())) {
            throw new AuthorizationException();
        }
    }
}
