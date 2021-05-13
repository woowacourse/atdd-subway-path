package wooteco.subway.auth.application;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

import javax.naming.AuthenticationException;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

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
        final Member member = memberDao.findByEmail(tokenRequest.getEmail())
                .orElseThrow(() -> new AuthorizationException("회원 정보를 찾지 못했습니다."));
        if (!member.haveSameInfo(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            throw new AuthorizationException("올바르지 않은 회원정보 입니다. 입력된 값: " + tokenRequest.getEmail());
        }
        return member.getId();
    }

    public Member findMemberByToken(String token) {
        jwtTokenProvider.validateToken(token);
        Long id = jwtTokenProvider.getIdFromPayLoad(token);
        return memberDao.findById(id);
    }
}
