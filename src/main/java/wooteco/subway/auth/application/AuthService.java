package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public String createToken(final TokenRequest tokenRequest) {
        return createToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }

    public String createToken(final String email, final String password){
        final Member member = memberDao.findByEmailAndPassword(email, password);
        return createTokenWithMemberId(member);
    }

    private String createTokenWithMemberId(final Member member){
        final String payload = String.valueOf(member.getId());
        return jwtTokenProvider.createToken(payload);
    }

    public Member findMemberByToken(final String token) {
        final String payload = jwtTokenProvider.getPayload(token);
        final Long memberId = Long.parseLong(payload);

        return memberDao.findById(memberId);
    }
}
