package wooteco.subway.auth.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.EmailNotFoundException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Optional<Member> optionalMember = memberDao.findByEmail(tokenRequest.getEmail());
        Member member = optionalMember.orElseThrow(EmailNotFoundException::new);
        member.checkValidPassword(tokenRequest.getPassword());

        String accessToken = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new TokenResponse(accessToken);
    }
}
