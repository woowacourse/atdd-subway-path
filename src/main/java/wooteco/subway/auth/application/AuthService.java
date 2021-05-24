package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.unauthorization.AuthorizationException;
import wooteco.subway.exception.notfound.EmailNotFoundException;
import wooteco.subway.exception.notfound.MemberNotFoundException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public LoginMember findByToken(String token) {
        String id = jwtTokenProvider.getPayload(token);
        Member member = memberDao.findById(Long.parseLong(id)).orElseThrow(MemberNotFoundException::new);
        return new LoginMember(member);
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = memberDao.findByEmail(tokenRequest.getEmail()).orElseThrow(EmailNotFoundException::new);
        member.checkValidPassword(tokenRequest.getPassword());

        String accessToken = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new TokenResponse(accessToken);
    }

    public void validateToken(String token) {
        if (jwtTokenProvider.validateToken(token)) {
            return;
        }
        throw new AuthorizationException();
    }
}
