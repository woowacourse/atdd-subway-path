package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;

import javax.validation.Valid;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    @Transactional
    public TokenResponse createToken(@Valid TokenRequest tokenRequest) {
        if (!checkValidLogin(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            throw new AuthorizationException();
        }

        String accessToken = jwtTokenProvider.createToken(tokenRequest.getEmail());
        return new TokenResponse(accessToken);
    }

    private boolean checkValidLogin(String email, String password) {
        if (memberDao.existByEmail(email)) {
            Member member = memberDao.findByEmail(email);
            return member.correctPassword(password);
        }
        return false;
    }

    public LoginMember findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        if (!memberDao.existByEmail(payload)) {
            throw new AuthorizationException();
        }
        Member member = memberDao.findByEmail(payload);
        return new LoginMember(member);
    }
}
