package wooteco.subway.auth.application;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.AuthorizationException;
import wooteco.subway.auth.infrastructure.CookieProvider;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {
    private final CookieProvider cookieProvider;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(CookieProvider cookieProvider, JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.cookieProvider = cookieProvider;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public ResponseCookie createCookie(String token) {
        return cookieProvider.createCookie(token);
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = memberDao.findByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword())
                .orElseThrow(() -> new AuthorizationException("[ERROR] 로그인 실패입니다."));
        String accessToken = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new TokenResponse(accessToken);
    }

    public Member findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    private Member findMember(String payload) {
        return memberDao.findById(Long.valueOf(payload)).orElseThrow(() ->
                new AuthorizationException("[ERROR] 존재하지 않는 회원입니다."));
    }
}
