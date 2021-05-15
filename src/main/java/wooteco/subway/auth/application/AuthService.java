package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

@Transactional
@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public String createToken(final TokenRequest tokenRequest) {
        return createToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }

    public String createToken(final String email, final String password) {
        if (memberDao.isExist(email, password)) {
            return jwtTokenProvider.createToken(email);
        }
        throw new AuthorizedException("존재하지 않는 유저입니다.");
    }

    public Member findMemberByToken(final String token) {
        final String email = jwtTokenProvider.getPayload(token);

        return memberDao.findByEmail(email)
                .orElseThrow(() -> new AuthorizedException("존재하지 않는 유저입니다."));
    }
}
