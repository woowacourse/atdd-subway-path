package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.MemberDao;
import wooteco.subway.domain.Member;
import wooteco.subway.exception.AuthException;
import wooteco.subway.exception.MemberNotFoundException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.request.TokenRequest;
import wooteco.subway.web.response.TokenResponse;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = memberDao.findByEmail(tokenRequest.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(tokenRequest.getEmail()));
        member.validatePassword(tokenRequest.getPassword());
        return new TokenResponse(jwtTokenProvider.createToken(tokenRequest.getEmail()));
    }

    public Member findMemberByToken(String accessToken) {
        String email = jwtTokenProvider.getPayload(accessToken);
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(email));
    }

    public void validateToken(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthException("토큰이 유효하지 않습니다.");
        }
    }
}
