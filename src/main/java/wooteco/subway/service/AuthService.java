package wooteco.subway.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import wooteco.auth.domain.Token;
import wooteco.exception.badRequest.ErrorResponse;
import wooteco.exception.badRequest.PasswordIncorrectException;
import wooteco.exception.notFound.MemberNotFoundException;
import wooteco.exception.unauthorized.InvalidTokenException;
import wooteco.auth.infrastructure.JwtTokenProvider;
import wooteco.auth.dao.MemberDao;
import wooteco.auth.domain.Member;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public Token login(String email, String password) {
        Member member = memberDao.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        if(member.invalidPassword(password)) {
            //TODO : errorResponse 에 정확한 에러 정보 넘겨주기
            throw new PasswordIncorrectException(new ErrorResponse());
        }
        return new Token(jwtTokenProvider.createToken(email));
    }

    public Member findMemberWithToken(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new InvalidTokenException();
        }
        String email = jwtTokenProvider.getPayload(accessToken);
        return memberDao.findByEmail(email).orElseThrow(MemberNotFoundException::new);
    }
}
