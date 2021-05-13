package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {

    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Long id = getIdWhenValidLogin(tokenRequest);
        String accessToken = jwtTokenProvider.createToken(id);
        return new TokenResponse(accessToken);
    }

    public long getIdWhenValidLogin(TokenRequest tokenRequest) {
        Member member = memberDao.findByEmail(tokenRequest.getEmail());
        if (!member.haveSameInfo(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            throw new AuthorizationException("입력된 값: " + tokenRequest.getEmail());
        }
        return member.getId();
    }

    public Member findMemberByToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException("만료된 토큰입니다.");
        }
        Long id = jwtTokenProvider.getIdFromPayLoad(token);
        return memberDao.findById(id);
    }
}
