package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.LoginMember;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.AuthorizationException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

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
                .orElseThrow(() -> new AuthorizationException("이메일이 존재하지 않습니다."));

        if (!member.isEqualToPassword(tokenRequest.getPassword())) {
            throw new AuthorizationException("비밀번호가 틀렸습니다!");
        }

        return new TokenResponse(jwtTokenProvider.createToken(tokenRequest.getEmail()));
    }

    public LoginMember findByToken(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }
        String email = jwtTokenProvider.getPayload(accessToken);
        Member member = memberDao.findByEmail(email)
                .orElseThrow(() -> new AuthorizationException("이메일이 존재하지 않습니다."));
        return new LoginMember(member);
    }
}
