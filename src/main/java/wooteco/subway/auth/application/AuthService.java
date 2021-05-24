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
        Member member = findMember(email, password);

        String memberId = String.valueOf(member.getId());
        return new TokenResponse(jwtTokenProvider.createToken(memberId));
    }

    private Member findMember(String email, String password) {
        Member member = findMemberByEmail(email);
        if (member.isNotValidPassword(password)) {
            throw new AuthException(AuthError.LOGIN_ERROR);
        }
        return member;
    }

    private Member findMemberByEmail(String email) {
        try {
            return memberDao.findByEmail(email);
        } catch (EmptyResultDataAccessException e) {
            throw new AuthException(AuthError.LOGIN_ERROR);
        }
    }
}
