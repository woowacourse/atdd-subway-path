package wooteco.subway.application;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Member;
import wooteco.subway.exception.UnauthorizedException;
import wooteco.subway.infrastructure.JwtTokenProvider;
import wooteco.subway.infrastructure.Sha256Hasher;
import wooteco.subway.presentation.dto.request.LoginMember;
import wooteco.subway.presentation.dto.request.TokenRequest;
import wooteco.subway.presentation.dto.response.TokenResponse;
import wooteco.subway.repository.MemberDao;

@Service
public class AuthService {

    private static final Sha256Hasher HASHER = new Sha256Hasher();

    private final MemberDao memberDao;
    private final JwtTokenProvider tokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider tokenProvider) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
    }

    public LoginMember getLoginMember(String accessToken) {
        if (!tokenProvider.validateToken(accessToken)) {
            throw new UnauthorizedException("토큰 유효기간 지남");
        }
        String email = tokenProvider.getPayload(accessToken);

        Member member = memberDao.findByEmail(email);
        return new LoginMember(member);
    }

    public TokenResponse login(TokenRequest tokenRequest) {
        Member member = getMember(tokenRequest);
        checkPassword(member, tokenRequest.getPassword());

        String accessToken = tokenProvider.createToken(tokenRequest.getEmail());
        return new TokenResponse(accessToken);
    }

    private Member getMember(TokenRequest tokenRequest) {
        try {
            return memberDao.findByEmail(tokenRequest.getEmail());
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new UnauthorizedException(
                    String.format("해당 이메일로 된 유저가 없습니다. 이메일 : %s", tokenRequest.getEmail()));
        }
    }

    public void checkPassword(Member member, String password) {
        String hashedPassword = HASHER.hashing(password);

        if (!member.samePassword(hashedPassword)) {
            throw new UnauthorizedException();
        }
    }
}
