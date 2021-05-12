package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
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
        checkInvalidLogin(tokenRequest);
        String accessToken = jwtTokenProvider.createToken(tokenRequest);
        return new TokenResponse(accessToken);
    }

    public void checkInvalidLogin(TokenRequest tokenRequest) {
        final Member member = memberDao.findByEmail(tokenRequest.getEmail());
        if (!member.haveSameInfo(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            throw new AuthorizationException("입력된 값: " + tokenRequest.getEmail());
        }
    }

    public wooteco.subway.member.dto.MemberResponse findMemberByToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException("만료된 토큰입니다.");
        }
        String email = jwtTokenProvider.getEmailFromPayload(token);
        return wooteco.subway.member.dto.MemberResponse.of(memberDao.findByEmail(email));
    }
}
