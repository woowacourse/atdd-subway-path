package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        checkInvalidLogin(tokenRequest.getEmail(), tokenRequest.getPassword());
        String accessToken = jwtTokenProvider.createToken(tokenRequest.getEmail());
        return new TokenResponse(accessToken);
    }

    public void checkInvalidLogin(String principal, String credentials) {
        memberDao.findByEmailAndPassword(principal, credentials)
            .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다."));
    }

    public MemberResponse findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    public MemberResponse findMember(String principal) {
        Member foundMember = memberDao.findByEmail(principal)
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));
        return new MemberResponse(foundMember);
    }
}
