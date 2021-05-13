package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.domain.AuthorizationPayLoad;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {

    // XXX :: MemberService와 MemberDao 중 선택의 근거
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    @Transactional(readOnly = true)
    public String createToken(final TokenRequest tokenRequest) {
        if(memberDao.isNotExistUser(tokenRequest.getEmail(), tokenRequest.getPassword())){
            throw new AuthorizedException("올바른 사용자가 아닙니다.");
        }
        return jwtTokenProvider.createToken(tokenRequest.getEmail());
    }

    public AuthorizationPayLoad getPayLoad(final String token) {
        jwtTokenProvider.validateToken(token);
        final String payload = jwtTokenProvider.getPayload(token);
        return new AuthorizationPayLoad(payload);
    }

    public Member findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return memberDao.findByEmail(payload);
    }

}
