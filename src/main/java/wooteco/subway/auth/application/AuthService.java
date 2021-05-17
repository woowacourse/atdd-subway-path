package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.InvalidMemberException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    @Transactional
    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = findMemberByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword());
        String token = jwtTokenProvider.createToken(member.getId().toString());
        return new TokenResponse(token);
    }

    private Member findMemberByEmailAndPassword(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new InvalidMemberException(email));
    }

    @Transactional(readOnly = true)
    public Member findMemberByToken(String token) {
        String id = getPayload(token);
        return memberDao.findById(Long.valueOf(id));
    }

    public String getPayload(String token) {
        return jwtTokenProvider.getPayload(token);
    }
}
