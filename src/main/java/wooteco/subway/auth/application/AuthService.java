package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;

@Service
public class AuthService {
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(String email) {
        return jwtTokenProvider.createToken(email);
    }

    public String getPayload(String token) {
        return jwtTokenProvider.getPayload(token);
    }

    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }
}
