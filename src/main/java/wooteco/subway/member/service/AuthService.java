package wooteco.subway.member.service;

import org.springframework.stereotype.Service;
import wooteco.subway.member.controller.request.TokenRequest;
import wooteco.subway.member.controller.response.TokenResponse;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.exception.AuthorizationException;
import wooteco.subway.member.infra.JwtTokenProvider;

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
                .orElseThrow(() -> new AuthorizationException("사용자를 찾을 수 없습니다."));

        if (!member.isEqualToPassword(tokenRequest.getPassword())) {
            throw new AuthorizationException("사용자를 찾을 수 없습니다.");
        }

        return new TokenResponse(jwtTokenProvider.createToken(tokenRequest.getEmail()));
    }

    public Member findMemberByToken(String accessToken) {
        String email = jwtTokenProvider.getPayload(accessToken);
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new AuthorizationException("사용자를 찾을 수 없습니다."));
    }

    public void validateToken(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }
    }
}
