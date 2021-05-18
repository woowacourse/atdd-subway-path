package wooteco.subway.member.service;

import org.springframework.stereotype.Service;
import wooteco.subway.member.controller.request.TokenRequest;
import wooteco.subway.member.controller.response.TokenResponse;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.exception.AuthException;
import wooteco.subway.member.exception.MemberNotFoundException;
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
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));
        member.validatePassword(tokenRequest.getPassword());
        return new TokenResponse(jwtTokenProvider.createToken(tokenRequest.getEmail()));
    }

    public Member findMemberByToken(String accessToken) {
        String email = jwtTokenProvider.getPayload(accessToken);
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));
    }

    public void validateToken(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthException("토큰이 유효하지 않습니다.");
        }
    }
}
