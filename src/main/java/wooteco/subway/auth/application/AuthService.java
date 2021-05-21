package wooteco.subway.auth.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.EmailNotFoundException;
import wooteco.subway.exception.PasswordMissMatchException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider,
        PasswordEncoder passwordEncoder, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        System.out.println("@#$@#$@#$@#$" + tokenRequest.getEmail());
        return memberDao.findByEmail(tokenRequest.getEmail())
            .map(member -> create(tokenRequest, member))
            .orElseThrow(EmailNotFoundException::new);
    }

    private TokenResponse create(TokenRequest tokenRequest, Member member) {
        if (passwordEncoder.matches(tokenRequest.getPassword(), member.getPassword())) {
            String accessToken = jwtTokenProvider.createToken(String.valueOf(member.getId()));
            return new TokenResponse(accessToken);
        }
        throw new PasswordMissMatchException();
    }
}
