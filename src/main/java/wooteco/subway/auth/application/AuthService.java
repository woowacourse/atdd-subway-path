package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.application.dto.LoginRequestDto;
import wooteco.subway.auth.exception.InvalidMemberException;
import wooteco.subway.auth.exception.InvalidTokenException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.auth.infrastructure.MemberAuthentication;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberAuthentication memberAuthentication;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberAuthentication memberAuthentication) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberAuthentication = memberAuthentication;
    }

    public String createToken(LoginRequestDto loginRequestDto) {
        if (memberAuthentication.authenticate(loginRequestDto.getEmail(), loginRequestDto.getPassword())) {
            return jwtTokenProvider.createToken(loginRequestDto.getEmail());
        }
        throw new InvalidMemberException();
    }

    public void validateToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException();
        }
    }

}
