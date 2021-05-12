package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.domain.Token;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.badRequest.ErrorResponse;
import wooteco.subway.exception.badRequest.PasswordIncorrectException;
import wooteco.subway.exception.notFound.MemberNotFoundException;
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

    public Token login(String email, String password) {
        Member member = memberDao.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        if(member.invalidPassword(password)) {
            //TODO : errorResponse 에 정확한 에러 정보 넘겨주기
            throw new PasswordIncorrectException(new ErrorResponse());
        }
        return new Token(jwtTokenProvider.createToken(email));
    }
}
