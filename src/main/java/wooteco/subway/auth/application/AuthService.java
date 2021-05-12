package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.InvalidMemberException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.auth.infrastructure.MemberAuthentication;

import java.io.IOException;

@Service
public class AuthService {

    private JwtTokenProvider jwtTokenProvider;
    private MemberAuthentication memberAuthentication;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberAuthentication memberAuthentication) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberAuthentication = memberAuthentication;
    }

    public String createToken(TokenRequest tokenRequest) {
        if (memberAuthentication.authenticate(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            return jwtTokenProvider.createToken(tokenRequest.getEmail());
        }
        throw new InvalidMemberException();
    }
}
