package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.LoginRequest;
import wooteco.subway.auth.exception.InvalidMemberException;
import wooteco.subway.auth.exception.InvalidTokenException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.auth.infrastructure.MemberAuthentication;

@Service
public class AuthService {

    private JwtTokenProvider jwtTokenProvider;
    private MemberAuthentication memberAuthentication;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberAuthentication memberAuthentication) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberAuthentication = memberAuthentication;
    }

    public String createToken(LoginRequest loginRequest) {
        if (memberAuthentication.authenticate(loginRequest.getEmail(), loginRequest.getPassword())) {
            return jwtTokenProvider.createToken(loginRequest.getEmail());
        }
        throw new InvalidMemberException();
    }

    public void validateToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException();
        }
    }
}
