package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;

@Service
public class AuthService {

    private final JwtTokenProvider tokenProvider;

    public AuthService(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        String email = tokenRequest.getEmail();
        String accessToken = tokenProvider.createToken(email);
        checkValidLogin(tokenRequest);
        return new TokenResponse(accessToken);
    }

    private void checkValidLogin(TokenRequest tokenRequest) {
        //login 관련 로직
    }
}
