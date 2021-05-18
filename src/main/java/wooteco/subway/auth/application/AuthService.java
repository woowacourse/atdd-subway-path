package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.AuthorizationException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.LoginMember;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = memberDao.findByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword())
                .orElseThrow(() -> new AuthorizationException("[ERROR] 로그인 실패입니다."));
        String accessToken = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new TokenResponse(accessToken);
    }

    public LoginMember findLoginMemberByToken(String token) {
        Long id = Long.valueOf(jwtTokenProvider.getPayload(token));
        validate(id);
        return new LoginMember(id);
    }

    private void validate(Long id) {
        if (memberDao.existById(id)) {
            return;
        }
        throw new AuthorizationException("[ERROR] 존재하지 않는 회원입니다.");
    }
}
