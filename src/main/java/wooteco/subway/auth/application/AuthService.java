package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.auth.IllegalTokenException;
import wooteco.subway.exception.auth.LoginFailEmailException;
import wooteco.subway.exception.auth.LoginWrongPasswordException;
import wooteco.subway.exception.member.NotRegisteredMemberException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {

    private final JwtTokenProvider tokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider tokenProvider, MemberDao memberDao) {
        this.tokenProvider = tokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        String email = tokenRequest.getEmail();
        String accessToken = tokenProvider.createToken(email);
        checkAvailableLogin(tokenRequest);
        return new TokenResponse(accessToken);
    }

    private void checkAvailableLogin(TokenRequest tokenRequest) {
        String email = tokenRequest.getEmail();
        String password = tokenRequest.getPassword();
        Member member = memberDao.findByEmail(email).orElseThrow(LoginFailEmailException::new);
        if (!member.getPassword().equals(password)) {
            throw new LoginWrongPasswordException();
        }
    }

    public String getPayLoad(String tokenName) {
        return tokenProvider.getPayload(tokenName);
    }

    public void checkAvailableToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new IllegalTokenException();
        }
    }
}
