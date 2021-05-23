package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.InvalidTokenException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.auth.dto.LoginMember;
import wooteco.subway.member.domain.Member;

import java.util.Optional;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Long memberId = findMemberId(tokenRequest);
        String accessToken = jwtTokenProvider.createToken(String.valueOf(memberId));
        return new TokenResponse(accessToken);
    }

    private Long findMemberId(TokenRequest tokenRequest) {
        Optional<Member> foundMember = Optional.ofNullable(memberDao.findByEmail(tokenRequest.getEmail())
                .orElseThrow(AuthorizationException::new));

        Member member = foundMember.get();
        if (member.hasDifferentPassword(tokenRequest.getPassword())) {
            throw new AuthorizationException();
        }
        return member.getId();
    }

    public LoginMember findMemberByToken(String token) {
        if (jwtTokenProvider.validateToken(token)) {
            String payload = jwtTokenProvider.getPayload(token);
            return new LoginMember(Long.valueOf(payload));
        }
        throw new InvalidTokenException();
    }
}
