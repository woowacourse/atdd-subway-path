package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public String createToken(final TokenRequest tokenRequest) {
        if (checkValidLogin(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            return jwtTokenProvider.createToken(tokenRequest.getEmail());
        }
        throw new IllegalArgumentException("적절하지 않은 사용자입력");
    }

    private boolean checkValidLogin(final String email, final String password) {
        return memberDao.isExist(email, password);
    }

    public Member findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return memberDao.findByEmail(payload);
    }

}
