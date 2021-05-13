package wooteco.auth.service;

import org.springframework.stereotype.Service;
import wooteco.auth.domain.Token;
import wooteco.exception.unauthorized.MemberAlreadyDeletedException;
import wooteco.exception.badRequest.ErrorResponse;
import wooteco.exception.badRequest.PasswordIncorrectException;
import wooteco.exception.unauthorized.InvalidTokenException;
import wooteco.auth.infrastructure.JwtTokenProvider;
import wooteco.auth.dao.MemberDao;
import wooteco.auth.domain.Member;
import wooteco.exception.unauthorized.LoginFailException;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;
    private final Member member;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao,
        Member member) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
        this.member = member;
    }


    public Token login(String email, String password) {
        Member member = memberDao.findByEmail(email).orElseThrow(LoginFailException::new);
        if(member.invalidPassword(password)) {
            throw new PasswordIncorrectException();
        }
        return new Token(jwtTokenProvider.createToken(member.getId().toString()));
    }

    public Member findMemberWithToken(String accessToken) {
        Long id = Long.valueOf(jwtTokenProvider.getPayload(accessToken));
        if(member.emptyMember()) {
            Member foundMember = memberDao.findById(id).orElseThrow(MemberAlreadyDeletedException::new);
            member.setMemberInfo(
                foundMember.getId(), foundMember.getEmail(),
                foundMember.getPassword(), foundMember.getAge()
            );
        }
        return member;
    }
}
