package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

import java.util.Optional;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public Optional<String> createToken(final TokenRequest tokenRequest) {
        if (checkValidLogin(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            return Optional.of(jwtTokenProvider.createToken(tokenRequest.getPassword()));
        }
        return Optional.empty();
    }

    private boolean checkValidLogin(final String email, final String password) {
        return memberDao.isExist(email, password);
    }

    public Optional<Member> findMemberByToken(String token) {
        final String payload = jwtTokenProvider.getPayload(token);
        return memberDao.findByPassword(payload);
    }
}
