package wooteco.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.exception.AuthError;
import wooteco.subway.auth.exception.AuthException;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isPreflight(request)) {
            return true;
        }
        String accessToken = AuthorizationExtractor.extract(request);
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthException(AuthError.REQUIRE_LOGIN_ERROR);
        }
        return true;
    }

    private boolean isPreflight(HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }
}
