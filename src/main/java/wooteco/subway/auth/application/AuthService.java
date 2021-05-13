package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

import java.util.Optional;

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

    public long  getIdWhenValidLogin(TokenRequest tokenRequest) {
        final Optional<Member> member = memberDao.findByEmail(tokenRequest.getEmail());
        if (!member.isPresent() ||
                !member.get().haveSameInfo(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            throw new AuthorizationException();
        }

        return member.get().getId();
    }

    public Member findMemberByToken(String token) {
        jwtTokenProvider.validateToken(token);
        Long id = jwtTokenProvider.getIdFromPayLoad(token);
        return memberDao.findById(id);
    }
}
