package wooteco.subway.auth.application;

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

        String userPassword = memberDao.getUserPassword(email)
                                       .orElseThrow(() -> new AuthException(AuthError.EMAIL_NOT_FOUND_ERROR));

        if (password.equals(userPassword)) {
            return new TokenResponse(jwtTokenProvider.createToken(email));
        }
        throw new AuthException(AuthError.LOGIN_ERROR);
    }

    public Member findMemberByToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthException(AuthError.TOKEN_EXPIRED);
        }
        String email = jwtTokenProvider.getPayload(token);

        return findMemberByEmail(email);
    }

    private Member findMemberByEmail(String email) {
        return memberDao.findByEmail(email)
                        .orElseThrow(() -> new AuthException(AuthError.EMAIL_NOT_FOUND_ERROR));
    }
}
