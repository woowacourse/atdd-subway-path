package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.auth.InvalidMemberException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = findMemberEmailAndPassword(tokenRequest);
        return new TokenResponse(jwtTokenProvider.createToken(member.getId().toString()));
    }

    private Member findMemberEmailAndPassword(TokenRequest tokenRequest) {
        return memberDao.findByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword())
                .orElseThrow(() -> new InvalidMemberException(tokenRequest.getEmail()));
    }

    @Transactional
    public LoginMember findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return new LoginMember(memberDao.findById(Long.valueOf(payload)));
    }

    public boolean validateToken(String token) {
        if (jwtTokenProvider.validateToken(token)) {
            return true;
        }
        return false;
    }
}
