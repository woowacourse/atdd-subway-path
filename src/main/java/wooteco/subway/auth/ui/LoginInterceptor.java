package wooteco.subway.auth.ui;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.exception.AuthError;
import wooteco.subway.auth.exception.AuthException;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = AuthorizationExtractor.extract(Objects.requireNonNull(request));
        if (jwtTokenProvider.validateToken(token)) {
            return true;
        }
        throw new AuthException(AuthError.INVALID_TOKEN);
    }
}
