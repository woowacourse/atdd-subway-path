package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;

@Service
public class AuthService {

    private JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        // Todo
//        if (checkInvalidLogin(tokenRequest.getEmail(), tokenRequest.getPassword())) {
//            throw new AuthorizationException();
//        }

        String accessToken = jwtTokenProvider.createToken(tokenRequest.getEmail());
        return new TokenResponse(accessToken);
    }
}
