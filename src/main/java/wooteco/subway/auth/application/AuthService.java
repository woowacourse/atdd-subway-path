package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.AuthorizationException;
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

    public TokenResponse createToken(TokenRequest tokenRequest) {
        if (checkInvalidLogin(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            throw new AuthorizationException("잘못된 이메일 혹은 비밀번호입니다.");
        }

        String accessToken = jwtTokenProvider.createToken(tokenRequest.getEmail());
        return new TokenResponse(accessToken);
    }

    public boolean checkInvalidLogin(String email, String password) {
        if (memberDao.existByEmail(email)) {
            Member member = memberDao.findByEmail(email);
            return member.incorrectPassword(password);
        }
        return true;
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
