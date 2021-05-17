package wooteco.subway.auth.application;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.AuthError;
import wooteco.subway.auth.exception.AuthException;
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
        String email = tokenRequest.getEmail();
        String password = tokenRequest.getPassword();
        findByEmailAndPassword(email, password);
        return new TokenResponse(jwtTokenProvider.createToken(email));
    }

    private Member findByEmailAndPassword(String email, String password) {
        try {
            return memberDao.findByEmailAndPassword(email, password);
        } catch (EmptyResultDataAccessException e) {
            throw new AuthException(AuthError.LOGIN_ERROR);
        }
    }

    public Member findMemberByToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthException(AuthError.TOKEN_EXPIRED_ERROR);
        }
        String email = jwtTokenProvider.getPayload(token);

        return findMemberByEmail(email);
    }

    private Member findMemberByEmail(String email) {
        try {
            return memberDao.findByEmail(email);
        } catch (EmptyResultDataAccessException e) {
            throw new AuthException(AuthError.EMAIL_NOT_FOUND_ERROR);
        }
    }
}
