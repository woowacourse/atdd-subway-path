package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.auth.domain.Token;
import wooteco.auth.infrastructure.JwtTokenProvider;
import wooteco.auth.exception.badRequest.ErrorResponse;
import wooteco.auth.exception.badRequest.PasswordIncorrectException;
import wooteco.auth.exception.notFound.MemberNotFoundException;
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
}
