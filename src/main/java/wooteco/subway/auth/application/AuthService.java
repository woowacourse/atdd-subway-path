package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.AuthorizationFailureException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.application.ObjectNotFoundException;
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
        Long id = obtainVerifiedId(tokenRequest);
        String accessToken = jwtTokenProvider.createToken(id);
        return new TokenResponse(accessToken);
    }

    private long obtainVerifiedId(TokenRequest tokenRequest) {
        Member member = memberDao.findByEmail(tokenRequest.getEmail())
            .orElseThrow(() -> new AuthorizationFailureException(String.format("없는 이메일: %s", tokenRequest.getEmail())));
        validateCorrectPassword(tokenRequest, member);
        return member.getId();
    }

    private void validateCorrectPassword(TokenRequest tokenRequest, Member member) {
        if (!member.checkPassword(tokenRequest.getPassword())) {
            throw new AuthorizationFailureException(String.format("비밀번호 불일치: %s", tokenRequest.getEmail()));
        }
    }

    public Member findMemberByToken(String token) {
        Long id = jwtTokenProvider.getIdFromPayLoad(token);
        return memberDao.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("유저를 찾는데 실패했습니다."));
    }
}
