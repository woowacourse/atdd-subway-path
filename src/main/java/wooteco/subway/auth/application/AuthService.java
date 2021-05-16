package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class AuthService {
    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final MemberDao memberDao, final JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(final TokenRequest tokenRequest) {
        if (!checkValidLogin(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            throw new AuthorizationException("등록되지 않은 사용자입니다.");
        }
        return new TokenResponse(jwtTokenProvider.createToken(tokenRequest.getEmail()));
    }

    private boolean checkValidLogin(final String email, final String password) {
        return memberDao.checkValidMember(email, password);
    }

    @Transactional(readOnly = true)
    public MemberResponse findMemberByToken(final String token) {
        String email = jwtTokenProvider.getEmail(token);
        Member member = memberDao.findByEmail(email);
        return MemberResponse.of(member);
    }

    public void isValidateToken(final String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException("올바르지 않은 토큰입니다.");
        }
    }
}
