package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.AuthorizationException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {

    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Long id = obtainVerifiedId(tokenRequest);
        String accessToken = jwtTokenProvider.createToken(id);
        return new TokenResponse(accessToken);
    }

    private long obtainVerifiedId(TokenRequest tokenRequest) {
        Member member = memberDao.findByEmail(tokenRequest.getEmail())
            .orElseThrow(() -> new AuthorizationException(String.format("없는 이메일: %s", tokenRequest.getEmail())));
        if (!member.checkPassword(tokenRequest.getPassword())) {
            throw new AuthorizationException(String.format("비밀번호 불일치: %s", tokenRequest.getEmail()));
        }
        return member.getId();
    }

    public Member findMemberByToken(String token) {
        Long id = jwtTokenProvider.getIdFromPayLoad(token);
        return memberDao.findById(id);
    }
}
